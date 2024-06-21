<template>
  <el-dialog :title="isAttrEditing ? '编辑属性' : '新增属性'"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :visible.sync="attrDialogVisible"
             :before-close="handleClose"
             width="700px"
             append-to-body
  >
    <el-form :model="form" :rules="rules" label-width="90px" ref="form" v-loading="dialoading">
      <el-form-item prop="name" label="属性名称">
        <el-input v-model="form.name" clearable placeholder="请输入属性名称"/>
      </el-form-item>
      <el-form-item prop="identifier" label="属性标识">
        <el-input v-model="form.identifier" clearable :disabled="isAttrEditing" placeholder="请输入属性标识"/>
      </el-form-item>
      <el-form-item prop="isRequired" label="是否必填">
        <el-switch v-model="form.isRequired" :active-value="true" :inactive-value="false"/>
      </el-form-item>
      <el-form-item prop="type" label="属性类型">
        <el-select v-model="form.type" clearable :disabled="isAttrEditing" placeholder="请选择属性类型" style="width: 100%" @change="typeChange">
          <el-option
            v-for="item in attrTypeMap"
            :key="item.key"
            :label="item.name"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="[0, 5].includes(form.type)" label="正则校验" prop="validationRule">
        <el-input
          v-model="form.validationRule"
          placeholder="请输入正则校验"
          autocomplete="off"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 5 }"
        />
      </el-form-item>
      <el-form-item v-if="[1, 2].includes(form.type)" label="最小值" prop="min">
        <el-input-number :precision="form.type === 2? 2: 0" v-model="form.min" clearable placeholder="请输入最小值" :controls="false"  class="bound-input" style="width:100%" />
      </el-form-item>
      <el-form-item v-if="[1, 2].includes(form.type)" label="最大值" prop="max">
        <el-input-number :precision="form.type === 2? 2: 0" v-model="form.max" clearable placeholder="请输入最大值" :controls="false"  class="bound-input" style="width:100%" />
      </el-form-item>
      <el-form-item v-if="form.type === 4" required label-width="90px">
        <div v-for="(item, index) in form.options" :key="index" >
          <el-row :gutter="6" style="margin-bottom: 8px">
            <el-col :span="22">
              <el-form-item label-width="0px" :key="item.key" :prop="'options.' + index"
                            :rules="[
                  { required: true, message: '枚举值不能为空', trigger: 'blur'},
                  { max: 200, message: '枚举值最大长度200个字符' }
                ]">
                <el-input v-model="form.options[index]" clearable placeholder="请输入枚举值" />
              </el-form-item>
            </el-col>
            <el-col :span="2" style="margin-top: 8px; cursor: pointer;">
              <i v-if="form.options.length > 1" class="el-icon-remove-outline" style="font-size: 26px; color: red" @click="delCurrentTarget(index)"></i>
            </el-col>
          </el-row>
        </div>
        <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="addEnum">增加枚举值</el-button>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :disabled="dialoading" @click="submit">确 定</el-button>
    </div>
  </el-dialog>

</template>

<script>


import { addAttr, editAttr } from "@/api/model";
import {resetObject} from "@/utils";

export default {
  name: 'Attribute',
  props: {
    attrDialogVisible:  {
      type: Boolean,
      required: true,
      default: false
    },
    isAttrEditing: {
      type: Boolean,
      required: true,
      default: false
    },
    modelId: {
      type: Number,
      required: true
    },
    attrForm: {
      type: Object,
      required: true
    }
  },

  data() {
    return {
      dialoading: false,
      attrTypeMap: [
        { key: 0, name: '文本' },
        { key: 1, name: '数字' },
        { key: 2, name: '浮点数' },
        { key: 3, name: '日期' },
        { key: 4, name: '枚举' },
        { key: 5, name: '密码' }
      ],
      form: {
        isRequired: true,
        name: '',
        identifier: '',
        type: '',
        validationRule: '',
        max: undefined,
        min: undefined,
        options: [''],
      },
      rules: {
        type: [ {required: true, message: '类型不能为空', trigger: 'change'} ],
        identifier: [
          { required: true, message: "属性标识不能为空", trigger: ['blur', 'change'] },
          { max: 50, message: "最大长度50个字符" },
          { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '请输入以字母开头、只允许输入字母数字和下划线' }
        ],
        name: [
          { required: true, message: "属性名称不能为空", trigger: ['blur', 'change'] },
          { max: 20, message: "最大长度20个字符" }
        ],
        validationRule: [
          { max: 100, message: "最大长度100个字符" }
        ],
        isRequired: [
          {required: true}
        ],
        max: [
          { required: true, message: '最大值不能为空', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              value < this.form.min? callback(new Error('最大值不能小于最小值')) : callback()
            },
            trigger: ['blur', 'change']
          }
        ],
        min: [
          { required: true, message: '最小值不能为空', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              value > this.form.max? callback(new Error('最小值不能大于最大值')) : callback()
            },
            trigger: ['blur', 'change']
          }
        ]
      }
    }

  },
  watch: {
    attrDialogVisible(newValue, oldValue) {
      if (newValue) {
        if (this.$parent.isAttrEditing) {
          this.form = {
            ...this.form,
            ...this.attrForm
          }

          if ([1, 2].includes(this.form.type)) {
            const jsonArray = this.attrForm.options.split(',')
            this.form.min = jsonArray[0]
            this.form.max = jsonArray[1]
          }
          if (this.form.type === 4) {
            this.form.options = this.attrForm.options.split(',')
          }
        } else {
          this.reset()
        }
      }
    }
  },
  created() {
  },

  methods: {
    handleClose() {
      this.$parent.attrDialogVisible = false
      this.dialoading = false
      setTimeout(() => {
        this.$parent.isAttrEditing = false
      }, 200)
      this.reset()
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    reset() {
      resetObject(this.form)
    },
    addEnum() {
      this.form.options.push('')
    },
    delCurrentTarget(index) {
      this.form.options.splice(index, 1)
    },
    typeChange() {
      this.$refs.form.clearValidate('validationRule')
      this.$refs.form.clearValidate('max')
      this.$refs.form.clearValidate('min')
      this.form.validationRule = ''
      this.form.max = ''
      this.form.min = ''
      this.form.options = ['']
    },
    submit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.dialoading = true
          const params = JSON.parse(JSON.stringify(this.form))
          if ([1, 2].includes(params.type)) {
            params.options = params.min + ',' + params.max
          } else if (params.type === 4) {
            params.options = params.options.join(',')
          } else {
            params.options = ''
          }

          params.modelId = this.modelId
          delete params.max
          delete params.min
          const Fn = this.isAttrEditing ? editAttr : addAttr
          Fn(params).then(response => {
            if (response.code === 0) {
              this.dialoading = false
              this.$parent.fetchAttrList()
              this.$message.success(`${this.isAttrEditing? '编辑成功' : '新增成功'}`)
              this.handleClose()
            }
          }).catch((err) => {
            this.dialoading = false
          })
        } else {
          return false
        }
      })
    }

  }
}
</script>
<style lang="scss" scoped>
  .itemEnum {
    margin-bottom: 16px;
  }

  ::v-deep .bound-input > .el-input > .el-input__inner {
    text-align: left;
  }
</style>

<template>
  <el-dialog title="唯一校验"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :visible.sync="validationDialogVisible"
             :before-close="handleClose"
             width="550px"
             append-to-body
  >
    <el-form :model="form" label-width="90px" ref="form" label-position="top" v-loading="dialoading">
      <el-form-item required label="模型属性">
        <div v-for="(item, index) in form.validateColumns" :key="index" class="itemEnum">
          <el-row :gutter="6">
            <el-col :span="21">
              <el-form-item label-width="0px" :key="item.id" :prop="'validateColumns.' + index + '.attributeList'"
                            :rules="[{
                  required: true,
                  message: '模型属性不能为空',
                  trigger: 'change',
                }]">
                <el-select v-model="item.attributeList" clearable  multiple :disabled="item.disabled" placeholder="请选择属性" style="width: 100%">
                  <el-option
                    v-for="item in attrList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col v-if="!item.disabled" :span="!item.disabled? 1 : 2" style="margin-top: 4px; cursor: pointer;" >
              <i class="el-icon-circle-check" title="新增" style="font-size: 26px; color: #409eff" @click="addCurrent(item)"></i>
            </el-col>
            <el-col v-if="!item.disabled" :span="!item.disabled? 1 : 2" style="margin-top: 4px; padding-left: 8px; cursor: pointer;">
              <i class="el-icon-remove-outline" title="减少" style="font-size: 26px; color: red" @click="delCurrentTarget(index)"></i>
            </el-col>
            <el-col v-else :span="2" style="margin-top: 4px; cursor: pointer;">
              <i class="el-icon-delete" title="删除" style="font-size: 24px; color: red" @click="delCurrent(item)"></i>
            </el-col>
          </el-row>
        </div>
        <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="addEnum">增加校验</el-button>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer"></div>
  </el-dialog>
</template>

<script>

import {addValidation, deleteValidation, validationList} from "@/api/model";

export default {
  name: 'Validation',
  props: {
    validationDialogVisible:  {
      type: Boolean,
      required: true,
      default: false
    },
    attrList: {
      type: Array,
      required: true,
      default: (() => [])
    },
    modelId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      dialoading: false,
      attributeNames: [],
      form: {
        validateColumns: [{id: '', attributeList: [], disabled: false}]
      }
    }
  },
  watch: {
    validationDialogVisible: {
      deep: true,
      handler(v) {
        this.fetchData()
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      validationList(this.modelId).then(response => {
        const attrData = response.data
        attrData.forEach((item => {
          item.disabled = true
        }))
        this.form.validateColumns = attrData
      })
    },
    // 关闭
    handleClose() {
      this.$parent.validationDialogVisible = false
      this.reset()
      this.dialoading = false
    },
    reset() {
      this.$refs.form.resetFields()
      this.form = {
        validateColumns: [{id: '', attributeList: [], disabled: false}]
      }
    },
    addEnum() {
      this.form.validateColumns.push({id: '', attributeList: [], disabled: false})
    },
    // 删除
    delCurrent(item) {
      this.$confirm('此操作将删除当前选择的唯一校验, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.dialoading = true
        deleteValidation(item.id).then((res) => {
          if(res.code === 0)
            this.$message.success('删除成功')
            this.fetchData()
            this.dialoading = false
        }).catch(() => {
            this.dialoading = false
        })
      })
    },
    delCurrentTarget(index) {
      this.form.validateColumns.splice(index, 1)
    },
    // 新增唯一校验
    addCurrent(item) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.dialoading = true
          const validateColumns = this.attrList.filter(item1 =>  item.attributeList.includes(item1.id)).map(item1 => item1.identifier).join(',')
          const params = {
            modelId: this.modelId,
            validateColumns,
          }
          addValidation(params).then((res) => {
            if (res.code === 0) {
              this.dialoading = false
              this.fetchData()
              this.$message.success(`新增成功`)
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
</style>

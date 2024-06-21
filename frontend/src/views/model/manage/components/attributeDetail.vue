<template>
  <div class="container">
    <el-drawer
      :visible.sync="detailDrawerVisible"
      :before-close="handleClose"
      size="40%"
    >
      <template #title>
        <span style="font-weight: bold;">属性详情</span>
      </template>
      <el-form :model="form" required label-width="90px" ref="form" v-loading="dialoading">
        <el-col :span="20">
          <el-form-item  label="属性标识">
            <el-input readonly v-model="form.identifier"/>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item  label="属性名称">
            <el-input readonly :value="form.name"/>
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item  label="是否必填">
            <el-input readonly :value="form.isRequired && '是' || '否'"/>
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item  label="属性类型">
            <el-input readonly :value="mapTypeStatus[form.type]"/>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item v-if="[0, 5].includes(form.type)" label="正则校验">
            <el-input readonly :value="form.validationRule"/>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item v-if="[1, 2].includes(form.type)" label="最小值">
            <el-input readonly :value="form.options[0]"/>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item v-if="[1, 2].includes(form.type)" label="最大值">
            <el-input readonly :value="form.options[1]"/>
          </el-form-item>
        </el-col>
        <template>
          <el-form-item v-if="form.type === 4" label="枚举值">
            <div v-for="(item, index) in form.options" :key="index" class="itemEnum">
              <el-row style="margin-bottom: 8px">
                <el-col :span="20">
                  <el-form-item label-width="0px" >
                    <el-input readonly :value="item" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form-item>
        </template>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
export default {
  name: 'AttributeDetail',
  props: {
    attrForm: {
      type: Object,
      required: true
    },
    detailDrawerVisible:  {
      type: Boolean,
      required: true,
      default: false
    }
  },
  watch: {
    attrForm: {
      handler(val) {
        if (val) {
          this.form = JSON.parse(JSON.stringify(val))
          this.form.options = this.form.options && this.form.options.split(",") || []
        }
      },
      deep: true
    }
  },
  data() {
    return {
      dialoading: false,
      mapTypeStatus: {
        0: '文本',
        1: '数字',
        2: '浮点',
        3: '日期时间',
        4: '枚举',
        5: '密码'
      },
      form: {}
    }
  },
  methods: {
    handleClose() {
      this.$parent.detailDrawerVisible = false
    }
  }
}
</script>

<style scoped>
.custom-drawer .el-drawer__title {
  font-weight: bold;
}

</style>

<template>
  <div class="app-container" style="display: flex">
    <el-dialog :title="isGroupEditing ? '编辑分组' : '新增分组'" width="600px" :visible.sync="dialogVisible" :before-close="(done) => handleClose(done, 'groupForm')">
      <el-form ref="groupForm" :model="groupForm"  :rules="groupRules">
        <el-form-item label="分组名称" label-width="120px" prop="name">
          <el-input v-model="groupForm.name" placeholder="请输入分组名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('groupForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('groupForm')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="isModelEditing ? '编辑模型' : '新增模型'" width="600px" :visible.sync="modelDialogVisible"  :before-close="(done) => handleClose(done, 'modelForm')">
      <el-form :model="modelForm" ref="modelForm" :rules="modelRules">
        <el-form-item label="模型名称" label-width="120px" prop="name">
          <el-input v-model="modelForm.name" placeholder="请输入模型名称"></el-input>
        </el-form-item>
        <el-form-item label="模型唯一标识" label-width="120px" prop="uniqueKey">
          <el-input :disabled="isModelEditing" v-model="modelForm.uniqueKey" placeholder="模型唯一标识"></el-input>
        </el-form-item>
        <el-form-item label="是否启用" label-width="120px" prop="isEnabled">
          <el-switch v-model="modelForm.isEnabled"></el-switch>
        </el-form-item>
        <el-form-item label="描述" label-width="120px" prop="description">
          <el-input type="textarea" v-model="modelForm.description" placeholder="请输入描述">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('modelForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('modelForm')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="新增关系" label-width="100px" :visible.sync="relationDialogVisible" :before-close="(done) => handleClose(done, 'relationForm')">
      <el-form :model="relationForm" ref="relationForm" :rules="relationRules">
        <el-form-item label="源模型" label-width="120px" prop="sourceId">
          <el-select v-model="relationForm.sourceId" placeholder="请选择源模型">
            <el-option v-if="currentModel" :key="currentModel.id" :label="currentModel.name" :value="currentModel.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="目标模型" label-width="120px" prop="targetId">
          <el-select v-model="relationForm.targetId" placeholder="请选择目标模型">
            <el-option v-for="item in allModelList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="关联类型" label-width="120px" prop="relationTypeId">
          <el-select v-model="relationForm.relationTypeId" placeholder="请选择活动区域">
            <el-option v-for="item in relationList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="关系约束" label-width="120px" prop="relationBind">
          <el-select v-model="relationForm.relationBind" placeholder="请选择关系约束">
            <el-option v-for="item in relationBindList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('relationForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('relationForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 唯一校验 -->
    <validation :validationDialogVisible="validationDialogVisible" :attrList="attrTableData" :modelId="+currentModelId"/>

    <!-- 模型属性 -->
    <attribute :attrDialogVisible="attrDialogVisible" :isAttrEditing="isAttrEditing" :modelId="+currentModelId" :attrForm="attrForm"/>

    <!-- 查看详情 -->
    <attribute-detail :detailDrawerVisible="detailDrawerVisible" :attrForm="attrForm"/>


    <div class="sidebar" style="width: 240px;border-right: 1px solid #ebeef5;">
      <div style="padding-left: 20px;">
        <el-button type="primary" size="small" @click="dialogVisible = true">新增分组</el-button>
      </div>
      <el-menu
        class="el-menu-vertical-demo"
        :collapse="isCollapse"
        :default-openeds="defaultOpened"
        @select="handleSelectModel"
        style="border-right: none;">
        <el-submenu
          v-for="menu in models"
          :key="menu.id  + ''"
          :index="menu.id  + ''">
          <template slot="title">
            <div class="dialog-title">
              <div>
                <i class="el-icon-s-order"></i>
                <span style="font-weight: bold;">{{ menu.name }}({{ menu.resourceModelList.length }})</span>
              </div>
              <div class="icon-container">
                <el-tooltip content="新增模型" placement="top">
                  <i class="el-icon-plus" style="color: blue;font-size: 15px;" @click="addModel(menu.id, $event)"></i>
                </el-tooltip>
                <el-tooltip content="编辑分组" placement="top" style="margin-left: 8px; margin-right: 8px">
                  <i class="el-icon-edit" style="color: blue;font-size: 15px;" @click="updateGroup(menu, $event)"></i>
                </el-tooltip>
                <el-tooltip content="删除分组" placement="top">
                  <i class="el-icon-delete" style="color: red;font-size: 15px;" @click="deleteGroup(menu.id, $event)"></i>
                </el-tooltip>
              </div>
            </div>
          </template>
          <el-menu-item-group v-if="menu.resourceModelList.some(model => model.isEnabled === true)">
            <template slot="title">已启用</template>
            <el-menu-item
              v-for="submenu in menu.resourceModelList.filter(model => model.isEnabled === true)"
              :key="submenu.id  + ''"
              :index="submenu.id + ''">
              <div class="submenu-item dialog-title" @mouseenter="showIcons(submenu.id)" @mouseleave="hideIcons(submenu.id)">
                {{ submenu.name }}
                <div v-if="isHovering(submenu.id)">
                  <el-tooltip content="编辑模型" placement="top" style="margin-left: 8px; margin-right: 8px">
                    <i class="el-icon-edit" style="color: blue;font-size: 15px;" @click="updateModel(menu.id, submenu, $event)"></i>
                  </el-tooltip>
                  <el-tooltip content="删除模型" placement="top">
                    <i class="el-icon-delete" style="color: red;font-size: 15px;" @click="deleteModel(submenu.id, $event)"></i>
                  </el-tooltip>
                </div>
              </div>
            </el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group v-if="menu.resourceModelList.some(model => model.isEnabled === false)">
            <template slot="title">未启用</template>
            <el-menu-item
              v-for="submenu in menu.resourceModelList.filter(model => model.isEnabled === false)"
              :key="submenu.id  + ''"
              :index="submenu.id + ''">
              <div class="submenu-item dialog-title" @mouseenter="showIcons(submenu.id)" @mouseleave="hideIcons(submenu.id)">
                {{ submenu.name }}
                <div v-if="isHovering(submenu.id)">
                  <el-tooltip content="编辑模型" placement="top" style="margin-left: 8px; margin-right: 8px">
                    <i class="el-icon-edit" style="color: blue;font-size: 15px;" @click="updateModel(menu.id, submenu, $event)"></i>
                  </el-tooltip>
                  <el-tooltip content="删除模型" placement="top">
                    <i class="el-icon-delete" style="color: red;font-size: 15px;" @click="deleteModel(submenu.id, $event)"></i>
                  </el-tooltip>
                </div>
              </div>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </div>

    <div v-if="currentModelId" class="main-content">
      <el-header style="height: 30px; padding: 0;">
        <el-menu @select="handleSelect" :default-active=currentIndex class="el-menu-demo custom-menu" mode="horizontal">
          <el-menu-item index='attr'>模型属性</el-menu-item>
          <el-menu-item index='relation'>模型关联</el-menu-item>
        </el-menu>
      </el-header>
      <el-main style="padding-left: 10px; padding-top: 12px">
        <el-button v-if="currentIndex === 'relation'" type="primary" size="small" @click="addModelRelation" plain>新增关系</el-button>
        <el-button v-if="currentIndex === 'attr'" type="primary" size="small" @click="attrDialogVisible = true" plain>新增属性</el-button>
        <el-button v-if="currentIndex === 'attr'" type="primary" size="small" @click="addModelValidation" plain>唯一校验</el-button>
        <div style="margin-bottom: 10px;display: flex;justify-content: flex-start;"></div>
        <el-table
          v-if="currentIndex === 'attr'"
          :data="attrTableData"
          border
          style="width: 100%">
          <el-table-column prop="name" label="属性名称"/>
          <el-table-column prop="identifier" label="属性标识"/>
          <el-table-column prop="type" :formatter="formatAttrType" label="属性类型"/>
          <el-table-column prop="isRequired" :formatter="formatBoolean" label="是否必填"/>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="small" type="text" @click="detailAttr(scope.row)">详情</el-button>
              <el-tooltip v-if="scope.row.identifier === 'instance_name'" content="固定属性，无法编辑" placement="top">
                <el-button disabled size="small" type="text">编辑</el-button>
              </el-tooltip>
              <el-tooltip v-if="scope.row.identifier === 'instance_name'" content="固定属性，无法删除" placement="top">
                <el-button disabled size="small" type="text">删除</el-button>
              </el-tooltip>
              <template v-else>
                <el-button size="small" type="text" @click="updateAttr(scope.$index, scope.row)">编辑</el-button>
                <el-button style="color: red"  size="small" type="text" @click="deleteAttr(scope.$index, scope.row)">删除</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
        <el-table
          v-if="currentIndex === 'relation'"
          :data="relationTableData"
          border
          style="width: 100%">
            <el-table-column prop="sourceModelName" label="源模型"/>
            <el-table-column prop="relationName" label="关系"/>
            <el-table-column prop="targetModelName" label="目标模型"/>
            <el-table-column prop="relationBindName" label="关系约束"/>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-tooltip v-if="String(scope.row.sourceId) !== currentModelId" content="被动关联，无法删除" placement="top">
                  <!-- 如果是目标端模型的话 不允许删除 -->
                  <el-button size="small" type="text" disabled>删除</el-button>
                </el-tooltip>
                <el-button v-else style="color: red" size="small" type="text" @click="deleteRelation(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
      </el-main>
    </div>
    <div v-else class="main-content">
      <el-empty :image-size="200"></el-empty>
    </div>
  </div>
</template>

<script>
import {
  addModel,
  addRelation, deleteAttr,
  deleteModel,
  deleteRelation,
  listAll, listAttr,
  listModel,
  listRelation,
  updateModel
} from '@/api/model'
import { add, drop, update } from '@/api/group'
import { MessageBox } from 'element-ui'
import { resetObject } from '@/utils'
import { list } from "@/api/relation"
import Validation from "@/views/model/manage/components/validation"
import Attribute from "@/views/model/manage/components/attribute"
import AttributeDetail from "@/views/model/manage/components/attributeDetail"

export default {
  name: 'Relation',
  components: {
    Validation, Attribute, AttributeDetail
  },
  data() {
    return {
      isCollapse: false,
      models: [],
      /**
       * 默认全部打开
       */
      defaultOpened: [],
      dialogVisible: false,
      groupForm: {
        id: '',
        name: ''
      },
      groupRules: {
        name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
      },
      isGroupEditing: false, // 分组是否编辑状态
      hoverMenuId: null,
      modelDialogVisible: false,
      isModelEditing: false,
      modelForm: {
        id: null,
        groupId: null,
        name: '',
        uniqueKey: '',
        isEnabled: false,
        description: ''
      },
      modelRules: {
        name: [{ required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 50, message: '模型名称长度必须在2到50个字符之间', trigger: 'blur' }],

        uniqueKey: [{ required: true, message: '请输入模型唯一标识', trigger: 'blur' },
          { min: 2, max: 50, message: '模型唯一标识长度必须在2到50个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '模型唯一标识只允许字母开头，包含字母，数字和下划线', trigger: 'change' }],

        description: [{ max: 500, message: '模型描述长度不能超过500位', trigger: 'blur' }],
        isEnabled: [{ required: true}]
      },
      currentModelId: '',
      /**
       * 默认打开属性
       */
      currentIndex: 'attr',
      relationTableData: [],
      relationDialogVisible: false,
      relationForm: {
        sourceId: null,
        targetId: null,
        relationTypeId: null,
        relationBind: null
      },
      relationRules: {
        sourceId: [{ required: true, message: '请选择源模型', trigger: 'blur' }],
        targetId: [{ required: true, message: '请选择目标模型', trigger: 'blur' }],
        relationTypeId: [{ required: true, message: '请选择关联类型', trigger: 'blur' }],
        relationBind: [{ required: true, message: '请选择关系约束', trigger: 'blur' }]
      },
      allModelList: [],
      relationBindList: [
        {
          value: 0,
          label: "一对一"
        },
        {
          value: 1,
          label: "一对多"
        },
        {
          value: 2,
          label: "多对多"
        }
      ],
      relationList: [],
      attrTableData: [],
      attrTypeMap: {
        0: '文本',
        1: '整数',
        2: '浮点数',
        3: '日期',
        4: '枚举',
        5: '密码'
      },
      validationDialogVisible: false,
      attrDialogVisible: false,
      isAttrEditing: false,
      attrForm: {
        isRequired: true,
        name: '',
        identifier: '',
        type: '',
        validationRule: '',
        max: undefined,
        min: undefined,
        options: '',
      },
      detailDrawerVisible: false
    }
  },
  created() {
    this.fetchMenuData() // 获取菜单数据
  },
  computed: {
    currentModel() {
      return this.allModelList.find(model => String(model.id) === this.currentModelId)
    }
  },
  methods: {
    // 新增唯一校验
    addModelValidation() {
      this.validationDialogVisible = true
    },
    async addModelRelation() {
      const [listAllResponse, listResponse] = await Promise.all([listAll(), list()])
      this.allModelList = listAllResponse.data
      this.relationList = listResponse.data
      this.relationDialogVisible = true
    },

    deleteRelation(index, row) {
      MessageBox.confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRelation(row.id).then((response) => {
          if (response.code === 0) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchListRelation()
          }
        })
      })
    },
    fetchListRelation() {
      listRelation(this.currentModelId).then(response => {
        const initiativeList = response.data.initiativeList
        const passiveList = response.data.passiveList
        this.relationTableData = [...initiativeList, ...passiveList]
      })
    },
    fetchAttrList() {
      const queryData = { modelId: Number(this.currentModelId) }
      listAttr(queryData).then(response => {
        this.attrTableData = response.data
      })
    },
    handleSelect(key) {
      this.currentIndex = key
      if (this.currentIndex === 'relation') {
        this.fetchListRelation()
      }
      if (this.currentIndex === 'attr') {
        this.fetchAttrList()
      }
    },
    handleSelectModel(key) {
      this.currentModelId = key
      this.currentIndex = 'attr'
      this.fetchAttrList()
      this.relationTableData = []
    },
    updateModel(groupId, model, event) {
      event.stopPropagation()
      this.modelForm = {
        ...this.modelForm,
        ...model,
        groupId: groupId
      }
      this.modelDialogVisible = true
      this.isModelEditing = true
    },

    deleteModel(modelId, event) {
      event.stopPropagation()
      MessageBox.confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteModel(modelId).then((response) => {
          if (response.code === 0) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchMenuData()
            // 清理当前模型
            if (String(modelId) === this.currentModelId) {
              this.currentModelId = ''
            }
          }
        })
      })
    },
    addModel(groupId, event) {
      event.stopPropagation()
      this.modelDialogVisible = true
      this.modelForm.groupId = groupId
    },
    showIcons(menuId) {
      this.hoverMenuId = menuId
    },
    hideIcons(menuId) {
      this.hoverMenuId = null
    },
    isHovering(menuId) {
      return this.hoverMenuId === menuId
    },
    calculateDefaultOpened() {
      this.defaultOpened = this.models.map(menu => menu.id + '')
    },
    fetchMenuData() {
      listModel(null).then(response => {
        this.models = response.data
        this.calculateDefaultOpened()
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          switch (formName) {
            case 'groupForm':
              this.doGroupForm()
              break
            case 'modelForm':
              this.doModelForm()
              break
            case 'relationForm':
              this.doRelationForm()
              break
          }
        } else {
          return false
        }
      })
    },
    doRelationForm() {
      // 提交表单
      addRelation(this.relationForm).then(response => {
        if (response.code === 0) {
          this.$message({
            message: '新增成功',
            type: 'success'
          })
          this.resetForm('relationForm')
          this.fetchListRelation()
        }
      })
    },
    doModelForm() {
      if (this.isModelEditing) {
        // 提交表单
        updateModel(this.modelForm).then(response => {
          if (response.code === 0) {
            this.$message({
              message: '编辑成功',
              type: 'success'
            })
            this.resetForm('modelForm')
            this.fetchMenuData()
          }
        })
      } else {
        // 提交表单
        addModel(this.modelForm).then(response => {
          if (response.code === 0) {
            this.$message({
              message: '新增成功',
              type: 'success'
            })
            this.resetForm('modelForm')
            this.fetchMenuData()
          }
        })
      }
    },
    doGroupForm() {
      if (this.isGroupEditing) {
        // 提交表单
        update(this.groupForm).then(response => {
          if (response.code === 0) {
            this.$message({
              message: '编辑成功',
              type: 'success'
            })
            this.resetForm('groupForm')
            this.fetchMenuData()
          }
        })
      } else {
        // 提交表单
        add(this.groupForm).then(response => {
          if (response.code === 0) {
            this.$message({
              message: '新增成功',
              type: 'success'
            })
            this.resetForm('groupForm')
            this.fetchMenuData()
          }
        })
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      if (formName === 'groupForm') {
        resetObject(this.groupForm)
        this.dialogVisible = false
        setTimeout(() => {
          this.isGroupEditing = false
        }, 100)
      } else if (formName === 'modelForm') {
        resetObject(this.modelForm)
        this.modelDialogVisible = false
        setTimeout(() => {
          this.isModelEditing = false
        }, 100)
      } else if (formName === 'relationForm') {
        resetObject(this.relationForm)
        this.relationDialogVisible = false
      }
    },

    handleClose(done, param) {
      this.resetForm(param)
      // 调用 done 函数以关闭对话框
      done()
    },
    deleteGroup(groupId, event) {
      // 阻止事件冒泡
      event.stopPropagation()
      MessageBox.confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        drop(groupId).then((response) => {
          if (response.code === 0) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchMenuData()
          }
        })
      })
    },
    updateGroup(menu, event) {
      event.stopPropagation()
      this.groupForm.name = menu.name
      this.groupForm.id = menu.id
      this.dialogVisible = true
      this.isGroupEditing = true
    },
    formatBoolean(row, column, cellValue) {
      return cellValue === 'true' || cellValue === true ? '是' : '否'
    },
    formatAttrType(row, column, cellValue) {
      return this.attrTypeMap[cellValue] || cellValue
    },
    detailAttr(row) {
      this.detailDrawerVisible = true
      this.attrForm = row
    },
    updateAttr(index, row) {
      this.isAttrEditing = true
      this.attrForm = row
      this.attrDialogVisible = true
    },
    deleteAttr(index, row) {
      MessageBox.confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAttr(+this.currentModelId, row.id).then((response) => {
          if (response.code === 0) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchAttrList()
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .el-submenu.is-opened>.el-submenu__title .el-submenu__icon-arrow {
    display: none;
  }

  ::v-deep .el-submenu>.el-submenu__title .el-submenu__icon-arrow {
  display: none;
  }

  .dialog-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .icon-container {
    display: none;
  }

  .dialog-title:hover .icon-container {
    display: flex;
  }

  .icon-container i {
    margin-left: 0;
    cursor: pointer;
  }

  .main-content {
    flex: 1;
    padding: 2px;
  }

  .custom-menu .el-menu-item {
    padding: 0 15px;
    height: 30px;
    line-height: 30px;
    font-weight: bold;
  }

  .custom-menu .el-menu-item:focus,
  .custom-menu .el-menu-item:hover,
  .custom-menu .el-menu-item.is-active {
    border-bottom: 2px solid #409EFF;
    margin-bottom: -2px;
    font-weight: bold;
  }

  .el-select {
    width: 100%;
  }
</style>

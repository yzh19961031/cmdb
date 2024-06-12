<template>
  <div class="app-container" style="display: flex;height: 100vh;">
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

    <div class="sidebar" style="width: 240px;border-right: 1px solid #ebeef5;">
      <div style="padding-left: 20px;">
        <el-button type="primary" size="small" @click="dialogVisible = true">新增分组</el-button>
      </div>
      <el-menu
        class="el-menu-vertical-demo"
        :collapse="isCollapse"
        :default-openeds="defaultOpened"
        @select="handleSelectModel">
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
          <el-menu-item
            v-for="submenu in menu.resourceModelList"
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
        </el-submenu>
      </el-menu>
    </div>

    <div v-if="currentModelId" class="main-content">
      <el-header class="custom-header">
        <el-menu :default-active="'1'" class="el-menu-demo custom-menu" mode="horizontal">
          <el-menu-item index="1">模型属性</el-menu-item>
          <el-menu-item index="2">模型关联</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
      </el-main>
    </div>
    <div v-else class="main-content">
      <el-empty :image-size="200"></el-empty>
    </div>
  </div>
</template>

<script>
import { addModel, deleteModel, listModel, updateModel } from '@/api/model'
import { add, drop, update } from '@/api/group'
import { MessageBox } from 'element-ui'
import { resetObject } from '@/utils'

export default {
  name: 'Relation',
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
        description: ''
      },
      modelRules: {
        name: [{ required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 5, message: '模型名称长度必须在2到50个字符之间', trigger: 'blur' }],

        uniqueKey: [{ required: true, message: '请输入模型唯一标识', trigger: 'blur' },
          { min: 2, max: 5, message: '模型唯一标识长度必须在2到50个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '模型唯一标识只允许字母开头，包含字母，数字和下划线', trigger: 'change' }],

        description: [{ max: 500, message: '模型描述长度不能超过500位', trigger: 'blur' }]
      },
      currentModelId: ''
    }
  },
  created() {
    this.fetchMenuData() // 获取菜单数据
  },
  methods: {
    handleSelectModel(key) {
      this.currentModelId = key
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
          }
        } else {
          return false
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

  .custom-header {
    height: 30px;
    padding: 0;
  }

  .custom-menu {
    height: 30px;
    line-height: 30px;
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
</style>

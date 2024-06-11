<template>
  <div class="app-container" style="display: flex;height: 100vh;">
    <el-dialog :title="isGroupEditing ? '编辑分组' : '新增分组'" width="600px" :visible.sync="dialogVisible"  :before-close="(done) => handleClose(done, 'groupForm')">
      <el-form :model="groupForm" ref="groupForm" :rules="groupRules">
        <el-form-item label="分组名称" label-width="120px" prop="name">
          <el-input v-model="groupForm.name" placeholder="请输入分组名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('groupForm')">取 消</el-button>
        <el-button type="primary" @click="submitForm('groupForm')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="sidebar" style="width: 240px;border-right: 1px solid #ebeef5;">
      <div style="padding-left: 20px;">
        <el-button type="primary" size="small" @click="dialogVisible = true">新增分组</el-button>
      </div>
      <el-menu
        class="el-menu-vertical-demo"
        :collapse="isCollapse"
        :default-openeds="defaultOpened">
        <el-submenu
          v-for="menu in models"
          :key="menu.id"
          :index="menu.id">
          <template slot="title">
            <div class="dialog-title">
              <div>
                <i class="el-icon-s-shop"></i>
                <span style="font-weight: bold;">{{ menu.name }}({{ menu.resourceModelList.length }})</span>
              </div>
              <div class="icon-container">
                <el-tooltip content="新增模型" placement="top">
                  <i class="el-icon-plus" style="color: blue;font-size: 15px;"></i>
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
            :key="submenu.id"
            :index="submenu.id">
            <div class="submenu-item dialog-title" @mouseenter="showIcons(submenu.id)" @mouseleave="hideIcons(submenu.id)">
              {{ submenu.name }}
              <div v-if="isHovering(submenu.id)">
                <el-tooltip content="编辑模型" placement="top" style="margin-left: 8px; margin-right: 8px">
                  <i class="el-icon-edit" style="color: blue;font-size: 15px;"></i>
                </el-tooltip>
                <el-tooltip content="删除模型" placement="top">
                  <i class="el-icon-delete" style="color: red;font-size: 15px;"></i>
                </el-tooltip>
              </div>
            </div>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
    <div class="main-content" style="flex: 1;padding: 20px;">
      <!-- 右侧主内容区 -->
      <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          prop="id"
          label="ID"
          width="180">
        </el-table-column>
        <el-table-column
          prop="name"
          label="关系名称"
          width="300">
        </el-table-column>
        <el-table-column
          prop="description"
          label="关系描述">
        </el-table-column>
        <el-table-column
          prop="createAt"
          label="创建时间">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import {list} from "@/api/relation";
import {listModel} from "@/api/model"
import {add, drop, update} from "@/api/group"
import {MessageBox} from "element-ui";

export default {
  name: 'Relation',
  data() {
    return {
      isCollapse: false,
      tableData: [],
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
      hoverMenuId: null
    }
  },
  created() {
    this.fetchData();
    this.fetchMenuData(); // 获取菜单数据
  },
  methods: {

    showIcons(menuId) {
      this.hoverMenuId = menuId;
    },

    hideIcons(menuId) {
      this.hoverMenuId = null;
    },
    isHovering(menuId) {
      return this.hoverMenuId === menuId;
    },
    calculateDefaultOpened() {
      this.defaultOpened = this.models.map(menu => menu.id);
    },
    fetchData() {
      list().then(response => {
        this.tableData = response.data;
      });
    },
    fetchMenuData() {
      listModel(null).then(response => {
        this.models = response.data;
        this.calculateDefaultOpened();
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.isGroupEditing) {
            // 提交表单
            update(this.groupForm).then(response => {
              if (response.code === 0) {
                this.$message({
                  message: '编辑成功',
                  type: 'success'
                })
                this.resetForm(formName)
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
                this.resetForm(formName)
                this.fetchMenuData()
              }
            })
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.groupForm.id = null
      this.groupForm.name = ''
      this.dialogVisible = false
      setTimeout(() => {
        this.isGroupEditing = false;
      }, 100);
    },

    handleClose(done, param) {
      this.resetForm(param)
      // 调用 done 函数以关闭对话框
      done();
    },
    deleteGroup(groupId, event) {
      // 阻止事件冒泡
      event.stopPropagation();
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
      event.stopPropagation();
      this.groupForm.name = menu.name;
      this.groupForm.id = menu.id;
      this.dialogVisible = true;
      this.isGroupEditing = true;
    }
  }
}
</script>

<style lang="scss">
  .el-submenu.is-opened>.el-submenu__title .el-submenu__icon-arrow {
    display: none;
  }

  .el-submenu>.el-submenu__title .el-submenu__icon-arrow {
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
</style>

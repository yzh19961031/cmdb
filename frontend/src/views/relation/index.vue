<template>
  <div class="app-container">
    <el-dialog title="新增关系类型" label-width="100px" :visible.sync="dialogVisible" :before-close="handleClose">
      <el-form :model="form" ref="form" :rules="rules">
        <el-form-item label="名称" label-width="120px" prop="name">
          <el-input v-model="form.name" placeholder="请输入关系类型名称">
          </el-input>
        </el-form-item>
        <el-form-item label="描述" label-width="120px" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入关系类型描述">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('form')">取 消</el-button>
        <el-button type="primary" @click="submitForm('form')">确 定</el-button>
      </div>
    </el-dialog>

    <div class="header-bar">
      <el-button type="primary" @click="addRelation" class="add-relation-btn">新增关系类型</el-button>
    </div>
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
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteRelation(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>


<script>

import {list, add, deleteRel} from "@/api/relation";
import {MessageBox} from "element-ui";

export default {
  name: 'Relation',
  data() {
    return {
      // 表格数据
      tableData: [],
      form: {
        name: '',
        description: ''
      },
      rules: {
        name: [{ required: true, message: '请输入关系类型名称', trigger: 'blur' }]
      },
      dialogVisible: false
    }
  },

  created() {
    this.fetchData()
  },

  methods: {
    deleteRelation(index, row) {
      MessageBox.confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRel(row.id).then((response) => {
          if (response.code === 0) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchData()
          }
        })
      })
    },
    fetchData() {
      list().then(response => {
        this.tableData = response.data
      })
    },
    addRelation() {
      this.dialogVisible = true
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 提交表单
          add(this.form).then(response => {
            if (response.code === 0) {
              this.$message({
                message: '新增成功',
                type: 'success'
              })
              this.resetForm(formName)
              this.dialogVisible = false
              this.fetchData()
            } else {

            }
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.dialogVisible = false
    },
    handleClose(done) {
      this.resetForm('form');
      done();
    }
  }

}
</script>

<style lang="scss" scoped>
  .header-bar {
    margin-bottom: 10px; /* 调整按钮和表格之间的间距 */
    display: flex;
    justify-content: flex-start; /* 将按钮靠左对齐 */
  }
</style>

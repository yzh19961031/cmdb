<template>
  <div class="app-container">
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
</template>


<script>

import {list} from "@/api/relation";

export default {
  name: 'Relation',
  data() {
    return {
      // 表格数据
      tableData: []
    }
  },

  created() {
    this.fetchData()
  },

  methods: {
    fetchData() {
      list().then(response => {
        this.tableData = response.data
      })
    }
  }

}
</script>

<style lang="scss" scoped>

</style>

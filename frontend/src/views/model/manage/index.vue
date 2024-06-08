<template>
  <div class="app-container">
    <el-container style="height: 750px; border: 1px solid #eee">
      <el-aside width="250px" style="background-color: rgb(238, 241, 246)">
        <el-menu>
          <el-submenu index="1">
            <template slot="title">分组1</template>
              <el-menu-item index="1-1">模型1</el-menu-item>
              <el-menu-item index="1-2">模型2</el-menu-item>
              <el-menu-item index="1-3">模型3</el-menu-item>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">分组2</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
              <el-menu-item index="2-3">选项3</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">

        </el-header>

        <el-main>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>

export default {
  name: 'ModelManage'
}
</script>

<style lang="scss" scoped>
  .el-menu-item {
    display: flex;
    align-items: center;
  }

  .el-menu-item .el-icon-edit,
  .el-menu-item .el-icon-delete {
    margin-left: auto;
  }

  .el-menu-item .el-icon-delete {
    margin-left: 5px;
  }
</style>

<template>
  <div class="app-container">
    <el-container>
      <el-header>
        <div style="display: flex; gap: 20px;">
          <el-link :class="{ active: isTableActive, largeText: true }" @click="showTable">表格形式</el-link>
          <el-link :class="{ active: !isTableActive, largeText: true }" @click="showTopology">拓扑形式</el-link>
        </div>
      </el-header>
      <el-main>
        <el-dialog title="新增关系" label-width="100px" :visible.sync="dialogVisible">
          <el-form :model="form" ref="form" :rules="rules">
            <el-form-item label="源模型" label-width="120px" prop="sourceId">
              <el-select v-model="form.sourceId" placeholder="请选择源模型">
                <el-option v-for="item in sourceModelList" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="目标模型" label-width="120px" prop="targetId">
              <el-select v-model="form.targetId" placeholder="请选择目标模型">
                <el-option v-for="item in targetModelList" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="关联类型" label-width="120px" prop="relationTypeId">
              <el-select v-model="form.relationTypeId" placeholder="请选择活动区域">
                <el-option v-for="item in relationList" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="关系约束" label-width="120px" prop="relationBind">
              <el-select v-model="form.relationBind" placeholder="请选择关系约束">
                <el-option v-for="item in relationBindList" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="resetForm('form')">取 消</el-button>
            <el-button type="primary" @click="submitForm('form')">确 定</el-button>
          </div>
        </el-dialog>
        <div v-if="isTableActive" id="table" class="table-container">
          <div class="header-bar">
            <el-button type="primary" @click="addRelation" class="add-relation-btn">新增关系</el-button>
          </div>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="sourceModelName" label="源模型"/>
            <el-table-column prop="relationName" label="关系"/>
            <el-table-column prop="targetModelName" label="目标模型"/>
            <el-table-column prop="relationBindName" label="关系约束"/>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="deleteRelation(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else id="topology"></div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import {addRelation, deleteRelation, listAll, listAllRelation, recursiveTopology} from "@/api/model";
import G6 from "@antv/g6";
import {MessageBox} from "element-ui";
import {list} from "@/api/relation";

export default {
  name: 'ModelRelation',
  data() {
    return {
      isTableActive: true,
      topologyData: {},
      graph: null,
      tableData: [],
      dialogVisible: false,
      form: {
        sourceId: null,
        targetId: null,
        relationTypeId: null,
        relationBind: null
      },
      rules: {
        sourceId: [{ required: true, message: '请选择源模型', trigger: 'blur' }],
        targetId: [{ required: true, message: '请选择目标模型', trigger: 'blur' }],
        relationTypeId: [{ required: true, message: '请选择关联类型', trigger: 'blur' }],
        relationBind: [{ required: true, message: '请选择关系约束', trigger: 'blur' }]
      },
      sourceModelList: [],
      targetModelList: [],
      relationList: [],
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
      ]
    };
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
      this.recursiveTopology()
      this.fetchModelList()
      this.fetchRelation()
    },
    fetchRelation() {
      list().then((response) => {
        this.relationList = response.data
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 提交表单
          addRelation(this.form).then(response => {
            if (response.code === 0) {
              this.$message({
                message: '新增成功',
                type: 'success'
              })
              this.resetForm(formName)
              this.dialogVisible = false
              this.fetchData()
              this.recursiveTopology()
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
    fetchModelList() {
      listAll().then((response) => {
        const data = response.data;
        this.sourceModelList = JSON.parse(JSON.stringify(data));
        this.targetModelList = JSON.parse(JSON.stringify(data));
      })
    },
    fetchData() {
      listAllRelation().then((response) => {
        this.tableData = response.data;
      });
    },
    showTable() {
      this.isTableActive = true;
      this.clearTopology(); // Clear the topology view
    },
    showTopology() {
      this.isTableActive = false;
      this.$nextTick(() => {
        this.generateGraph(); // Ensure the DOM is updated before generating the graph
      });
    },
    recursiveTopology() {
      recursiveTopology().then((response) => {
        this.topologyData = response.data;
        if (!this.isTableActive) {
          this.generateGraph(); // Generate graph if already in topology view
        }
      });
    },
    generateGraph() {
      const { nodes, edges } = this.topologyData;
      const container = document.getElementById("topology");
      if (!container) return; // Ensure the container exists
      const width = container.scrollWidth;
      const height = container.scrollHeight || 500;

      // Destroy the existing graph if it exists
      if (this.graph) {
        this.graph = null;
      }

      this.graph = new G6.Graph({
        container: "topology",
        width,
        height,
        fitView: true,
        animate: true,
        maxZoom: 3,
        minZoom: 0.18,
        fitCenter: true,
        modes: {
          default: ["drag-canvas", "zoom-canvas", "drag-node"],
        },
        defaultNode: {
          type: "rect",
          zIndex: 0,
          anchorPoints: [
            [0.5, 0],
            [0.5, 1],
            [0, 0.5],
            [1, 0.5],
          ],
          labelCfg: {
            position: "center",
            style: {
              fontSize: 8, // Adjust font size to fit within smaller nodes
            },
          },
          style: {
            fill: "#e6f7ff",
            lineWidth: 0,
            radius: 10,
            stroke: "#e0e0e0",
            width: 40, // Set the width of the nodes
            height: 18, // Set the height of the nodes
          },
        },
        defaultEdge: {
          style: {
            endArrow: {
              path: G6.Arrow.vee(5, 10, 5),
              d: 5,
              fill: "#e0e0e0",
            },
          },
          labelCfg: {
            autoRotate: true,
            style: {
              fill: "#1890ff",
              fontSize: 7,
              background: {
                fill: "#ffffff",
                stroke: "#9EC9FF",
                padding: [2, 2, 2, 2],
                radius: 2,
              },
            },
          },
        },
        layout: {
          type: "radial",
          linkDistance: 200,
        },
      });
      this.graph.data({ nodes, edges });
      this.graph.render();

      if (typeof window !== "undefined")
        window.onresize = () => {
          if (!this.graph || this.graph.get("destroyed")) return;
          if (!container || !container.scrollWidth || !container.scrollHeight)
            return;
          this.graph.changeSize(container.scrollWidth, container.scrollHeight);
        };
    },

    clearTopology() {
      const container = document.getElementById("topology");
      if (container) {
        container.innerHTML = ''; // Clear the container
      }
      if (this.graph) {
        this.graph = null;
      }
    },

    addRelation() {
      this.dialogVisible = true
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
            this.fetchData()
            this.recursiveTopology()
          }
        })
      })
    },
  },
};
</script>

<style lang="scss" scoped>
.active {
  color: #409EFF; /* Element UI primary color */
  font-weight: bold;
}

.largeText {
  font-size: 16px; /* Adjust font size as needed */
}

.table-container {
  margin-top: -10px; /* 调整上边距 */
}

.header-bar {
  margin-bottom: 10px; /* 调整按钮和表格之间的间距 */
  display: flex;
  justify-content: flex-start; /* 将按钮靠左对齐 */
}

.el-select {
  width: 100%;
}

</style>

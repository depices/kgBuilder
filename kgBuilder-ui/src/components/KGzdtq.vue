<template>
  <el-dialog
    title="知识图谱"
    :visible.sync="dialogVisible"
    width="70%"
    customClass="flowHelp"
  >
    <el-alert
      style="margin:10px"
      title="自动抽取"
      type="success"
      description="自动抽取三元组关系"
    >
    </el-alert>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="案件事实描述">
        <el-input type="textarea" v-model="form.fact" rows="6" :autosize="{ minRows: 6, maxRows: 10}"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">三元组抽取</el-button>
        <el-button @click="dialogVisible=!dialogVisible">清空</el-button>
      </el-form-item>


    </el-form>
  </el-dialog>
</template>

<script>
  import { kgBuilderApi } from "@/api";
  export default {
    data() {
      return {
        dialogVisible: false,
        form:{
          fact:""
        }
      };
    },
    components: {},
    methods: {
      init() {
        this.dialogVisible = true;
      },
      onSubmit(){
        let data = this.form;
        kgBuilderApi.getZdtq(data).then(result => {
          console.log(result)
          if (result.code == 200) {
            this.dialogVisible=false;
            this.$message({
              message: "操作成功",
              type: "success"
            });
          }
        });
      }
    }
  };
</script>

<style>
  .flowHelp {
    height: 80%;
  }
</style>

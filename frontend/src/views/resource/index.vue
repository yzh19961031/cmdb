<template>
  <div>
    <h1>Model Name: {{ $route.path }}</h1>
    <h1>Model Name: {{ modelName }}</h1>
  </div>
</template>

<script>
import {modelDetail} from "@/api/model";

export default {
  name: 'Resource',
  data() {
    return {
      modelId: null,
      modelName: ''
    }
  },
  created() {
    const array = this.$route.path.split('/')
    this.modelId = array[array.length - 1]
    modelDetail(this.modelId).then(response => {
      this.modelName = response.data.name
    })
  }
}
</script>

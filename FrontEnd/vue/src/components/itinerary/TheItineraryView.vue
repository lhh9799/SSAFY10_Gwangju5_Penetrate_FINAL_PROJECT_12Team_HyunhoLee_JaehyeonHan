<template>
  <div class='sidebar'>
    <div class="col-6">
      <div class="alert alert-info" role="alert">
        <strong>선택된 장소:</strong>
        <ul>
        <!-- <li v-for="location in selectedLocation" :key="location.contentType">
          {{ location.title }}
        </li> -->
        </ul>
        <!-- <button type="button" class="btn btn-outline-primary mb-3" @click="add">추가</button> -->
      </div>
    </div>
  </div>
</template>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
}

</style>
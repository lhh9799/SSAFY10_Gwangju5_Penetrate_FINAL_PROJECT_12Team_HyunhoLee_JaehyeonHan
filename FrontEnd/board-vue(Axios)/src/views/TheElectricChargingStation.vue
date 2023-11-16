<script setup>
import axios from "axios";
import { ref, onMounted } from "vue";

// const url = import.meta.env.VITE_ELECTRIC_CHARGING_STATION_URL;
// const serviceKey = import.meta.env.VITE_OPEN_API_SERVICE_KEY;
const { VITE_ELECTRIC_CHARGING_STATION_URL, VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const estations = ref([]);

onMounted(() => {
  getEstations();
});

const getEstations = () => {
  axios.get(VITE_ELECTRIC_CHARGING_STATION_URL, {
    params: {
      serviceKey: VITE_OPEN_API_SERVICE_KEY,
      pageNo: 1,
      numOfRows: 20,
      zcode: 11
    }
  })  
    .then(({ data }) => {
      console.log(data);
      estations.value = data.items[0].item;
    })
    .catch((error) => {
      console.log(error)
    });
}
</script>

<template>
  <div class="container text-center mt-3">
    <div class="alert alert-success" role="alert">전기차 충전소</div>
    <table class="table table-hover">
      <thead>
        <tr class="text-center" v-for='station in estations' :key='station.statId'>
          <th scope="col">{{ station.statNm }}</th>
          <th scope="col">{{ station.statId }}</th>
          <th scope="col">{{ station.stat }}</th>
          <th scope="col">{{ station.addr }}</th>
          <th scope="col">{{ station.lat }}</th>
          <th scope="col">{{ station.lng }}</th>
        </tr>
      </thead>
      <tbody>
        <tr class="text-center">
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
mark.purple {
  background: linear-gradient(to top, #c354ff 20%, transparent 30%);
}
</style>

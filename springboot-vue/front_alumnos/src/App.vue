<script setup>
import { ref, onMounted} from 'vue';
import axios from 'axios';

const alumnos = ref([]); // Definimos una variable reactiva para almacenar los alumnos
const cargarAlumnos = async () => {
    const response = await axios.get('http://localhost:8081/alumnos/traer-lumnos');//traer todos los alumnos
    alumnos.value = response.data;
    console.log(alumnos.value); 
}
onMounted(cargarAlumnos); // Llamamos a la función cargarAlumnos cuando el componente se monta
</script>

<template>
<div class="container">
  <button class="btn btn-primary">boton</button>
</div>
</template>

<style scoped>
</style>

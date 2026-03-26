<script setup>
import { computed, ref } from 'vue';


const props = defineProps({
  carrera: {
    type: String, required: true
  },
  datos: {
    type: Array, required: true
  }
})

const emit = defineEmits(['editar', 'eliminar'])

const mostrarTodos = ref(false)
const LIMITE = 5



const busqueda = ref('')
const datosFiltrados = computed(() =>
  props.datos.filter(alumno =>
    `${alumno.nombre} ${alumno.apellido}`
      .toLowerCase()
      .includes(busqueda.value.toLowerCase().trim())
  )
)
const datosMostrados = computed(() =>
  mostrarTodos.value ? datosFiltrados.value : datosFiltrados.value.slice(0, LIMITE)
)

</script>

<template>
  <div class="tabla-carrera" v-if="datos.length > 0">
    <div class="carrera-header">
      <span class="carrera-badge">{{ datosFiltrados.length }} alumno{{ datosFiltrados.length !== 1 ? 's' : '' }}</span>
      <h2>{{ carrera }}</h2>
      <div class="header-line"></div>
    </div>
    <!-- Buscador -->
    <div class="buscador-wrapper">
      <span class="buscador-icon"></span>
      <input v-model="busqueda" type="text" class="buscador-input" placeholder="Buscar por nombre o apellido..." />
      <button v-if="busqueda" class="buscador-limpiar" @click="busqueda = ''">✕</button>
    </div>
    <p v-if="datosFiltrados.length === 0" class="sin-resultados">
      No se encontró ningún alumno con "{{ busqueda }}"
    </p>

    <div class="tabla-wrapper">
      <table>
        <thead>
          <tr>
            <th>Imagen</th>
            <th>Nombre</th>
            <th>Apellidos</th>
            <th>Carrera</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in datosMostrados" :key="index" class="fila-alumno">
            <td class="td-img">
              <img
                :src="item.imagenURL || 'https://ui-avatars.com/api/?name=' + item.nombre + '+' + item.apellido + '&background=a78bfa&color=fff'"
                :alt="item.nombre" />
            </td>
            <td>{{ item.nombre }}</td>
            <td>{{ item.apellido }}</td>
            <td><span class="carrera-chip">{{ item.carrera }}</span></td>
            <td class="td-email">{{ item.email }}</td>
            <td>{{ item.telefono }}</td>

            <td>
              <button @click="emit('eliminar', item.id)" class="btn btn-danger mx-2"><i
                  class="bi bi-trash2"></i></button>
              <button @click="emit('editar', item)" class="btn btn-warning"><i class="bi bi-pencil-fill"></i></button>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- Boton para ver mas -->
      <div class="ver-mas-wrapper" v-if="datosFiltrados.length > LIMITE">
        <button class="btn-ver-mas" @click="mostrarTodos = !mostrarTodos">
          {{ mostrarTodos ? '▲ Mostrar menos' : `▼ Mostrar más (${datosFiltrados.length - LIMITE} restantes)` }}
        </button>
      </div>
    </div>
  </div>
</template>



<style scoped>
.tabla-carrera {
  margin-bottom: 48px;
  animation: fadeSlideIn 0.4s ease both;
}

@keyframes fadeSlideIn {
  from {
    opacity: 0;
    transform: translateY(16px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ── Header ── */
.carrera-header {
  position: relative;
  margin-bottom: 16px;
  padding-left: 20px;
}

.carrera-header h2 {
  font-size: 1.3rem;
  font-weight: 700;
  color: #1e3a8a;
  margin: 0 0 6px 0;
  text-align: left;
}

.carrera-badge {
  display: inline-block;
  font-size: 0.72rem;
  font-weight: 600;
  background: #ede9fe;
  color: #7c3aed;
  border-radius: 20px;
  padding: 2px 10px;
  margin-bottom: 4px;
  letter-spacing: 0.03em;
}

.header-line {
  height: 3px;
  width: 60px;
  background: linear-gradient(90deg, #a78bfa, #3b82f6);
  border-radius: 4px;
}

/* ── Tabla wrapper ── */
.tabla-wrapper {
  overflow-x: auto;
  border-radius: 14px;
  box-shadow: 0 4px 20px rgba(30, 58, 138, 0.08);
  border: 1px solid #e5e7eb;
  overflow-y: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  font-size: 0.9rem;
}

/* ── Encabezados ── */
thead tr {
  background: linear-gradient(135deg, #8274cf 0%, #6366f1 100%);
}

th {
  color: #ffffff;
  font-weight: 600;
  text-align: center;
  padding: 13px 16px;
  white-space: nowrap;
  letter-spacing: 0.03em;
  font-size: 0.82rem;
  text-transform: uppercase;
}

/* ── Filas ── */
tbody tr {
  border-bottom: 1px solid #f3f4f6;
  transition: background 0.2s ease, transform 0.15s ease;
}

tbody tr:last-child {
  border-bottom: none;
}

tbody tr:hover {
  background-color: #f5f3ff;

}

td {
  text-align: center;
  vertical-align: middle;
  padding: 12px 16px;
  color: #374151;
}

/* ── Imagen ── */
.td-img img {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #a78bfa;
  box-shadow: 0 2px 6px rgba(167, 139, 250, 0.35);
  transition: transform 0.2s ease;
}

.td-img img:hover {
  transform: scale(1.15);
}

/* ── Email ── */
.td-email {
  font-size: 0.82rem;
  color: #6b7280;
}

/* ── Chip de carrera ── */
.carrera-chip {
  display: inline-block;
  background: #ede9fe;
  color: #5b21b6;
  border-radius: 20px;
  padding: 3px 10px;
  font-size: 0.78rem;
  font-weight: 600;
  white-space: nowrap;
}

/* ── Filas alternas ── */
tbody tr:nth-child(even) {
  background-color: #fafafa;
}

tbody tr:nth-child(even):hover {
  background-color: #f5f3ff;
}

.ver-mas-wrapper {
  text-align: center;
  margin-top: 12px;
}

.btn-ver-mas {
  background: linear-gradient(135deg, #a78bfa, #6366f1);
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 24px;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s ease, transform 0.15s ease;
}

.btn-ver-mas:hover {
  opacity: 0.88;
  transform: translateY(-1px);
}
.buscador-wrapper {
  display:flex;
  
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  background: #ffffff;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  padding: 8px 14px;
  transition: border-color 0.2s ease;
}

.buscador-wrapper:focus-within {
  border-color: #a78bfa;
  box-shadow: 0 0 0 3px rgba(167, 139, 250, 0.15);
}

.buscador-input {
  border: none;
  outline: none;
  width: 100%;
  font-size: 0.9rem;
  color: #374151;
  background: transparent;
}

.buscador-limpiar {
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  font-size: 0.85rem;
  padding: 0 4px;
  transition: color 0.2s;
}

.buscador-limpiar:hover {
  color: #ef4444;
}

.sin-resultados {
  text-align: center;
  color: #9ca3af;
  font-size: 0.88rem;
  padding: 16px 0;
}
</style>
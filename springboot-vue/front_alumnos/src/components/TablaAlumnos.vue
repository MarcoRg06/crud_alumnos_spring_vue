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

const emit = defineEmits(['editar', 'eliminar', 'imprimir'])

const imprimirTabla = () => {
  emit('imprimir', { carrera: props.carrera, datos: props.datos })
}

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
      <div class="header-content">
        <div class="header-badge">
          <span>{{ datosFiltrados.length }} alumn{{ datosFiltrados.length !== 1 ? 'os' : 'o' }}</span>
        </div>
        <h2>{{ carrera }}</h2>
      </div>
      <div class="header-line"></div>
    </div>

    <div class="buscador-wrapper">
      <svg xmlns="http://www.w3.org/2000/svg" class="buscador-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="11" cy="11" r="8"></circle>
        <path d="m21 21-4.35-4.35"></path>
      </svg>
      <input v-model="busqueda" type="text" class="buscador-input" placeholder="Buscar por nombre o apellido..." />
      <button v-if="busqueda" class="buscador-limpiar" @click="busqueda = ''">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>
    </div>

    <p v-if="datosFiltrados.length === 0" class="sin-resultados">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"></circle>
        <path d="M8 15h8"></path>
        <path d="M9 9h.01"></path>
        <path d="M15 9h.01"></path>
      </svg>
      <span>No se encontró ningún alumn{{ busqueda ? 'o' : 'a' }} con "{{ busqueda }}"</span>
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
                :src="item.imagenURL || 'https://ui-avatars.com/api/?name=' + item.nombre + '+' + item.apellido + '&background=3b82f6&color=fff'"
                :alt="item.nombre" />
            </td>
            <td class="td-nombre">{{ item.nombre }}</td>
            <td class="td-apellido">{{ item.apellido }}</td>
            <td><span class="carrera-chip">{{ item.carrera }}</span></td>
            <td class="td-email">{{ item.email }}</td>
            <td class="td-telefono">{{ item.telefono }}</td>

            <td>
              <div class="acciones">
                <button @click="emit('eliminar', item.id)" class="btn btn-eliminar" title="Eliminar">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    <line x1="10" y1="11" x2="10" y2="17"></line>
                    <line x1="14" y1="11" x2="14" y2="17"></line>
                  </svg>
                </button>
                <button @click="emit('editar', item)" class="btn btn-editar" title="Editar">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="ver-mas-wrapper" v-if="datosFiltrados.length > LIMITE">
        <button class="btn-ver-mas" @click="mostrarTodos = !mostrarTodos">
          <span>{{ mostrarTodos ? '▲ Mostrar menos' : `▼ Mostrar más (${datosFiltrados.length - LIMITE} restantes)` }}</span>
        </button>
      </div>
    </div>

    <div class="imprimir-wrapper">
      <button @click="imprimirTabla" class="btn btn-imprimir" title="Imprimir tabla">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="6 9 6 2 18 2 18 9"></polyline>
          <path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"></path>
          <rect x="6" y="14" width="12" height="8"></rect>
        </svg>
        <span>Imprimir</span>
      </button>
    </div>
  </div>

</template>



<style scoped>
.imprimir-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.btn-imprimir {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  padding: 10px 20px;
  color: #475569;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.btn-imprimir svg {
  width: 18px;
  height: 18px;
}

.btn-imprimir:hover {
  background: #1e3a8a;
  border-color: #1e3a8a;
  color: white;
}

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

.carrera-header {
  margin-bottom: 20px;
  padding-left: 4px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.header-badge {
  display: inline-block;
  font-size: 0.72rem;
  font-weight: 600;
  background: #dbeafe;
  color: #1e3a8a;
  border-radius: 20px;
  padding: 4px 12px;
  letter-spacing: 0.03em;
}

.carrera-header h2 {
  font-size: 1.35rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.header-line {
  height: 4px;
  width: 80px;
  background: #1e3a8a;
  border-radius: 4px;
}

.buscador-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  max-width: 400px;
}

.buscador-icon {
  position: absolute;
  left: 14px;
  width: 20px;
  height: 20px;
  color: #94a3b8;
  pointer-events: none;
}

.buscador-input {
  width: 100%;
  padding: 14px 40px 14px 44px;
  background: #ffffff;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 0.95rem;
  color: #1e293b;
  transition: all 0.25s ease;
  outline: none;
}

.buscador-input::placeholder {
  color: #94a3b8;
}

.buscador-input:focus {
  border-color: #1e3a8a;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.buscador-limpiar {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.buscador-limpiar svg {
  width: 18px;
  height: 18px;
  color: #94a3b8;
}

.buscador-limpiar:hover svg {
  color: #ef4444;
}

.sin-resultados {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  text-align: center;
  color: #64748b;
  font-size: 0.95rem;
  padding: 40px 20px;
  background: #f8fafc;
  border-radius: 12px;
  margin-bottom: 16px;
}

.sin-resultados svg {
  width: 32px;
  height: 32px;
  opacity: 0.5;
}

.tabla-wrapper {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  font-size: 0.9rem;
}

thead tr {
  background: #1e3a8a;
}

th {
  color: #ffffff;
  font-weight: 600;
  text-align: center;
  padding: 16px 14px;
  white-space: nowrap;
  letter-spacing: 0.03em;
  font-size: 0.78rem;
  text-transform: uppercase;
}

tbody tr {
  border-bottom: 1px solid #f1f5f9;
  transition: background 0.2s ease;
}

tbody tr:last-child {
  border-bottom: none;
}

tbody tr:hover {
  background: #f8fafc;
}

tbody tr:nth-child(even) {
  background-color: #fafbfc;
}

tbody tr:nth-child(even):hover {
  background: #f8fafc;
}

td {
  text-align: center;
  vertical-align: middle;
  padding: 14px 12px;
  color: #334155;
}

.td-nombre,
.td-apellido {
  font-weight: 500;
}

.td-email {
  color: #64748b;
  font-size: 0.82rem;
}

.td-telefono {
  color: #64748b;
  font-size: 0.85rem;
}

.td-img img {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #1e3a8a;
  box-shadow: 0 2px 6px rgba(59, 130, 246, 0.25);
  transition: transform 0.2s ease;
}

.td-img img:hover {
  transform: scale(1.1);
}

.carrera-chip {
  display: inline-block;
  background: #dbeafe;
  color: #2563eb;
  border-radius: 20px;
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 600;
  white-space: nowrap;
}

.acciones {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.btn {
  border: none;
  border-radius: 10px;
  padding: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.btn svg {
  width: 18px;
  height: 18px;
}

.btn-eliminar {
  background: #fef2f2;
  color: #ef4444;
}

.btn-eliminar:hover {
  background: #ef4444;
  color: white;
  transform: scale(1.1);
}

.btn-editar {
  background: #dbeafe;
  color: #1e3a8a;
}

.btn-editar:hover {
  background: #1e3a8a;
  color: white;
  transform: scale(1.1);
}

.ver-mas-wrapper {
  text-align: center;
  padding: 16px;
  border-top: 1px solid #f1f5f9;
}

.btn-ver-mas {
  background: #1e3a8a;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 24px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.btn-ver-mas:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .tabla-wrapper {
    border-radius: 12px;
  }
  
  th, td {
    padding: 12px 8px;
    font-size: 0.8rem;
  }
  
  .td-img img {
    width: 36px;
    height: 36px;
  }
}
</style>
<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import swal from 'sweetalert2';
import TablaAlumnos from './components/TablaAlumnos.vue';
import Login from './components/Login.vue';

const carreras = [
  'Ingenieria en Mecatronica',
  'Ingenieria en Gestion Empresarial',
  'Ingenieria Industrial',
  'Ingenieria Civil',
  'Ingenieria en Sistemas Computacionales',
  'Licenciatura en Administración',
  'Licenciatura en Arquitectura',
  'Licenciatura en Contador Público'
]
const filtrarPorCarrera = (carrera) => {
  return alumnos.value.filter(alumno => alumno.carrera === carrera);
}
const alumnos = ref([]);
const token = ref(localStorage.getItem('token') || null);
if (token.value) axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
const nuevoAlumno = ref({
  nombre: '',
  apellido: '',
  carrera: '',
  telefono: '',
  imagenURL: '',
  email: ''
});

const errores = ref({
  nombre: '',
  apellido: '',
  telefono: '',
  email: ''
});
const editado = ref(false);
const carreraSeleccionada = ref(carreras[0]);
const cargarAlumnos = async () => {
  const response = await axios.get('http://localhost:8081/alumnos/traer-alumnos');
  alumnos.value = response.data;
}

const onLoginSuccess = async () => {
  token.value = localStorage.getItem('token')
  axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
  await cargarAlumnos()
}

const logout = () => {
  localStorage.removeItem('token')
  token.value = null
  delete axios.defaults.headers.common['Authorization']
}
const validarCampos = () =>{

  let valido = true;
  errores.value = {
    nombre: '',
    apellido: '',
    telefono: '',
    email: ''
  };

  const soloLetras = /^[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+$/;
  const soloNumeros = /^[0-9]{10}$/;
  const soloEmail = /^[a-zA-Z0-9._%+-]+@(gmail\.com|hotmail\.com|outlook\.com|tlaxiaco\.tecnm\.mx)$/;

  if(!soloLetras.test((nuevoAlumno.value.nombre || "").trim())){
    swal.fire({
      icon: 'warning',
      text: 'Datos invalidos en Nombre',
      showConfirmButton: false,
      timer: 2000
    });
    nuevoAlumno.value.nombre = '';
    valido = false;
  }
  if(!soloLetras.test((nuevoAlumno.value.apellido || "").trim())){
     swal.fire({
      icon: 'warning',
      text: 'Datos invalidos en Apellidos',
      showConfirmButton: false,
      timer: 2000
    });
    nuevoAlumno.value.apellido = '';
    valido = false;

  }
 if(!soloNumeros.test((nuevoAlumno.value.telefono || "").trim())){
     swal.fire({
      icon: 'warning',
      text: 'Datos invalidos en Telefono',
      showConfirmButton: false,
      timer: 2000
    });
    nuevoAlumno.value.telefono = '';
    valido = false;
  }
  if(!soloEmail.test((nuevoAlumno.value.email || "").trim())){
     swal.fire({
      icon: 'warning',
      text: 'Datos invalidos en Correo electronico',
      showConfirmButton: false,
      timer: 2000
    });
    nuevoAlumno.value.email = '';
    valido = false;
  }
  
  return valido;
 
}

const agregarAlumno = async () => {

  if(!validarCampos()){
    return;
  }

  if (editado.value) {
    await axios.put(`http://localhost:8081/alumnos/editar-alumno/${nuevoAlumno.value.id}`, nuevoAlumno.value);
    editado.value = false;
  
    swal.fire({
      icon: 'success',
      title: 'Alumno Actualizado Correctamente',
      showConfirmButton: false,
      timer: 1500
    });
    
  } else {
    await axios.post('http://localhost:8081/alumnos/insertar-alumno', nuevoAlumno.value);
    swal.fire({
      icon: 'success',
      title: 'Alumno Agregado Correctamente',
      showConfirmButton: false,
      timer: 1500
    });
  }
  await cargarAlumnos();
  nuevoAlumno.value = {
    nombre: '',
    apellido: '',
    carrera: '',
    telefono: '',
    imagenURL: '',
    email: ''
  };


}
const editarAlumnos = (alumno) => {
  console.log('Editando alumno:', alumno);
  swal.fire({
    title: '¿Deseas editar este alumno?',
    text: `${alumno.nombre} ${alumno.apellido}`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#1e3a8a',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, editar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      Object.assign(nuevoAlumno.value, alumno)
      editado.value = true
    }
  })
}
const eliminarAlumno = async (id) => {
  swal.fire({
    title: '¿Estás seguro de eliminar el alumno?',
    text: "¡No podrás revertir esto!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#1e3a8a',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminarlo'
  }).then(async (result) => {
    if (result.isConfirmed) {
      await eliminarAlumnoPorId(id);
      swal.fire(
        'Eliminado!',
        'El alumno ha sido eliminado.',
        'success'
      )
    }
  })

}
const imprimirAlumnos = async ({ carrera, datos }) => {
  const printWindow = window.open('', '_blank');
  
  const logoArriba = new URL('./images/ArribaIEncabezado.png', import.meta.url).href;
  const logoAbajo = new URL('./images/Abajo.png', import.meta.url).href;

  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8">
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        @page {
          size: letter;
          margin: 15mm; 
        }

        body {
          font-family: Arial, sans-serif;
          color: #333;
          -webkit-print-color-adjust: exact !important;
          print-color-adjust: exact !important;
        }

        .footer-fixed {
          position: fixed;
          bottom: 0;
          left: 0;
          width: 100%;
        }
        .footer-img { 
          width: 100%;
          height: 75px;
          object-fit: contain;
          display: block;
        }

        table { 
          width: 100%;
          border-collapse: collapse;
        }

        thead { display: table-header-group; }
        tfoot { display: table-footer-group; }
        tr { page-break-inside: avoid; }

        .header-cell {
          border: none !important;
          background-color: transparent !important;
          padding-bottom: 20px; 
        }
        .header-img { 
          width: 100%;
          height: 110px;
          object-fit: contain;
          display: block;
        }
        h1 { 
          color: #1e3a8a;
          text-align: center;
          margin-top: 15px;
          margin-bottom: 5px;
          font-size: 22px;
          text-transform: uppercase;
        }

        .col-headers th { 
          background-color: #1e3a8a !important;
          color: white !important;
          padding: 12px 8px;
          font-size: 13px;
          border: 1px solid #2563eb;
        }

        tbody td { 
          border: 1px solid #ddd;
          padding: 8px;
          text-align: center;
          font-size: 12px;
        }

        tbody tr:nth-child(even) { 
          background-color: #f9fafb !important; 
        }

        .footer-space {
          height: 85px;
          border: none !important;
          background-color: transparent !important;
        }
      </style>
    </head>
    <body>

      <div class="footer-fixed">
        <img src="${logoAbajo}" class="footer-img">
      </div>

      <table>
        <thead>
          <tr>
            <th colspan="4" class="header-cell">
              <img src="${logoArriba}" class="header-img">
              <h1>${carrera}</h1>
            </th>
          </tr>
          <tr class="col-headers">
            <th>Nombre</th>
            <th>Apellidos</th>
            <th>Email</th>
            <th>Teléfono</th>
          </tr>
        </thead>

        <tbody>
          ${datos.map(alumno => `
            <tr>
              <td>${alumno.nombre || ''}</td>
              <td>${alumno.apellido || ''}</td>
              <td>${alumno.email || 'N/A'}</td>
              <td>${alumno.telefono || ''}</td>
            </tr>
          `).join('')}
        </tbody>

        <tfoot>
          <tr>
            <td colspan="4" class="footer-space">&nbsp;</td>
          </tr>
        </tfoot>
      </table>

    </body>
    </html>
  `;

  printWindow.document.write(html);
  printWindow.document.close();
  printWindow.focus();
  setTimeout(() => {
    printWindow.print();
  }, 800);
};
const eliminarAlumnoPorId = async (id) => {
  try {
    await axios.delete(`http://localhost:8081/alumnos/eliminar-alumnos/${id}`);
    swal.fire({
        icon: 'success',
        title: 'Alumno Eliminado Correctamente',
        showConfirmButton: false,
        timer: 1500
    });
    console.log(`Alumno con ID ${id} eliminado`);
    await cargarAlumnos();
  } catch (error) {
    console.error('Error al eliminar el alumno:', error);
    swal.fire({
      icon: 'error',
      title: 'Error al eliminar el alumno',
      text: 'No se pudo eliminar el alumno.',
    });
  }
}

onMounted(() => { if (token.value) cargarAlumnos(); });


</script>

<template>
  <Login v-if="!token" @login-success="onLoginSuccess" />

  <div v-if="token" class="main-container">
    <header class="app-header">
      <div class="header-content">
        <div class="logo-section">
          <div class="logo-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 10v6M2 10l10-5 10 5-10 5z"></path>
              <path d="M6 12v5c0 2 2 3 6 3s6-1 6-3v-5"></path>
            </svg>
          </div>
          <div class="logo-text">
            <h1>Control de Alumnos</h1>
            <span>Instituto Tecnológico de Tlaxiaco</span>
          </div>
        </div>
        <button class="btn-logout" @click="logout">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
            <polyline points="16 17 21 12 16 7"></polyline>
            <line x1="21" y1="12" x2="9" y2="12"></line>
          </svg>
          Cerrar sesión
        </button>
      </div>
    </header>

    <div class="content-wrapper">
      <section class="form-section">
        <div class="section-header">
          <div class="header-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="8.5" cy="7" r="4"></circle>
              <line x1="20" y1="8" x2="20" y2="14"></line>
              <line x1="23" y1="11" x2="17" y2="11"></line>
            </svg>
          </div>
          <h2>{{ editado ? 'Editar Alumno' : 'Nuevo Alumno' }}</h2>
        </div>
        
        <form @submit.prevent="agregarAlumno" class="form-grid">
          <div class="form-row">
            <div class="form-group">
              <label>Nombre</label>
              <input type="text" placeholder="Inserte su Nombre" maxlength="30" v-model="nuevoAlumno.nombre" required>
            </div>
            <div class="form-group">
              <label>Apellidos</label>
              <input type="text" placeholder="Apellido Paterno y Materno" maxlength="30" v-model="nuevoAlumno.apellido" required>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Carrera</label>
              <select required v-model="nuevoAlumno.carrera">
                <option value="" disabled>Selecciona una Opción</option>
                <option value="Ingenieria en Mecatronica">Ingenieria en Mecatronica</option>
                <option value="Ingenieria en Gestion Empresarial">Ingenieria en Gestion Empresarial</option>
                <option value="Ingenieria Industrial">Ingenieria Industrial</option>
                <option value="Ingenieria Civil">Ingenieria Civil</option>
                <option value="Ingenieria en Sistemas Computacionales">Ingenieria en Sistemas Computacionales</option>
                <option value="Licenciatura en Contador Público">Licenciatura en Contador Público</option>
                <option value="Licenciatura en Arquitectura">Licenciatura en Arquitectura</option>
                <option value="Licenciatura en Administración">Licenciatura en Administración</option>
              </select>
            </div>
            <div class="form-group">
              <label>Teléfono</label>
              <input type="text" placeholder="953*******" maxlength="10" v-model="nuevoAlumno.telefono" required>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Correo electrónico</label>
              <input type="text" placeholder="user@tlaxiaco.tecnm.mx" maxlength="64" v-model="nuevoAlumno.email">
            </div>
            <div class="form-group">
              <label>Imagen URL</label>
              <input type="text" placeholder="Enlace URL" v-model="nuevoAlumno.imagenURL">
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-submit">
              <svg v-if="editado" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                <polyline points="17 21 17 13 7 13 7 21"></polyline>
                <polyline points="7 3 7 8 15 8"></polyline>
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
              {{ editado ? 'Actualizar Alumno' : 'Agregar Alumno' }}
            </button>
          </div>
        </form>
      </section>

      <section class="tables-section">
        <div class="tabs-container">
          <button 
            v-for="carrera in carreras" 
            :key="carrera"
            :class="['tab-btn', { active: carreraSeleccionada === carrera }]"
            @click="carreraSeleccionada = carrera"
          >
            {{ carrera }}
          </button>
        </div>
        <TablaAlumnos
          :carrera="carreraSeleccionada"
          :datos="filtrarPorCarrera(carreraSeleccionada)"
          @editar="editarAlumnos"
          @eliminar="eliminarAlumno"
          @imprimir="imprimirAlumnos"
        ></TablaAlumnos>
      </section>
    </div>
  </div>

</template>

<style scoped>
.main-container {
  min-height: 100vh;
  background: #f8fafc;
}

.app-header {
  background: #ffffff;
  border-bottom: 1px solid #e2e8f0;
  padding: 16px 24px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 14px;
}

.logo-icon {
  width: 44px;
  height: 44px;
  background: #1e3a8a;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon svg {
  width: 24px;
  height: 24px;
  color: #ffffff;
}

.logo-text h1 {
  font-size: 1.3rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.logo-text span {
  font-size: 0.8rem;
  color: #64748b;
}

.btn-logout {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  color: #475569;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.btn-logout svg {
  width: 18px;
  height: 18px;
}

.btn-logout:hover {
  background: #fee2e2;
  border-color: #fecaca;
  color: #dc2626;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  padding: 32px 24px;
}

.form-section {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 28px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f1f5f9;
}

.header-icon {
  width: 44px;
  height: 44px;
  background: #1e3a8a;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-icon svg {
  width: 22px;
  height: 22px;
  color: #ffffff;
}

.section-header h2 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.form-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 0.85rem;
  font-weight: 600;
  color: #475569;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.form-group input,
.form-group select {
  padding: 14px 16px;
  background: #ffffff;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  color: #1e293b;
  font-size: 0.95rem;
  transition: all 0.25s ease;
  outline: none;
}

.form-group input::placeholder {
  color: #94a3b8;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #1e3a8a;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-group select {
  cursor: pointer;
}

.form-actions {
  margin-top: 8px;
}

.btn-submit {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  max-width: 280px;
  padding: 16px 24px;
  background: #1e3a8a;
  border: none;
  border-radius: 12px;
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
}

.btn-submit svg {
  width: 20px;
  height: 20px;
}

.btn-submit:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

.tables-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.tabs-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 4px;
  background: #f1f5f9;
  border-radius: 12px;
}

.tab-btn {
  padding: 10px 16px;
  background: transparent;
  border: none;
  border-radius: 8px;
  color: #64748b;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.tab-btn:hover {
  color: #1e3a8a;
  background: #e2e8f0;
}

.tab-btn.active {
  background: #1e3a8a;
  color: white;
  box-shadow: 0 2px 8px rgba(30, 58, 138, 0.3);
}

@media (max-width: 768px) {
  .app-header {
    padding: 12px 16px;
  }
  
  .logo-text span {
    display: none;
  }
  
  .content-wrapper {
    padding: 20px 16px;
  }
  
  .form-section {
    padding: 20px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .btn-submit {
    max-width: 100%;
  }

  .tabs-container {
    overflow-x: auto;
    flex-wrap: nowrap;
    padding-bottom: 8px;
  }

  .tab-btn {
    white-space: nowrap;
    font-size: 0.8rem;
    padding: 8px 12px;
  }
}
</style>
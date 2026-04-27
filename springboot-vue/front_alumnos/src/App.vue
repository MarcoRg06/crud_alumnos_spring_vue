<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import swal from 'sweetalert2';
import TablaAlumnos from './components/TablaAlumnos.vue';

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
const alumnos = ref([]); // Definimos una variable reactiva para almacenar los alumnos
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
const editado = ref(false); // Variable para controlar si se está editando un alumno
const cargarAlumnos = async () => {
  const response = await axios.get('http://localhost:8081/alumnos/traer-alumnos');
  //const response = await axios.get('https://crud-alumnos-spring.uc.r.appspot.com/alumnos/traer-alumnos');//traer todos los alumnos
  alumnos.value = response.data;
  console.log(alumnos.value);
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
  
    // Si se está editando un alumno, actualizamos el alumno
    await axios.put(`http://localhost:8081/alumnos/editar-alumno/${nuevoAlumno.value.id}`, nuevoAlumno.value);
    //await axios.put(`https://crud-alumnos-spring.uc.r.appspot.com/alumnos/editar-alumno/${nuevoAlumno.value.id}`, nuevoAlumno.value);
    editado.value = false; // Reiniciamos la variable de edición
  
   
      swal.fire({
      icon: 'success',
      title: 'Alumno Actualizado Correctamente',
      showConfirmButton: false,
      timer: 1500
    });
    
  } else {
    // Si no se está editando, agregamos un nuevo alumno
    //await axios.post('https://crud-alumnos-spring.uc.r.appspot.com/alumnos/insertar-alumno', nuevoAlumno.value);
    await axios.post('http://localhost:8081/alumnos/insertar-alumno', nuevoAlumno.value);
    swal.fire({
      icon: 'success',
      title: 'Alumno Agregado Correctamente',
      showConfirmButton: false,
      timer: 1500
    });
  }
  await cargarAlumnos(); // Recargamos la lista de alumnos después de agregar uno nuevo
  nuevoAlumno.value = { // Limpiamos el formulario
    nombre: '',
    apellido: '',
    carrera: '',
    telefono: '',
    imagenURL: '',
    email: ''
  };


}
const editarAlumnos = (alumno) => {
 swal.fire({
    title: '¿Deseas editar este alumno?',
    text: `${alumno.nombre} ${alumno.apellido}`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
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
    confirmButtonColor: '#3085d6',
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

        /* --- PIE DE PÁGINA FIJO AL FONDO DE LA HOJA --- */
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

        /* --- ESTILOS DEL ENCABEZADO --- */
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

        /* --- ESTILOS DE LA TABLA DE DATOS --- */
        .col-headers th { 
          background-color: #6366f1 !important;
          color: white !important;
          padding: 12px 8px;
          font-size: 13px;
          border: 1px solid #4f46e5;
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

        /* --- ESPACIADOR FANTASMA --- */
        /* Esta celda invisible evita que las filas se impriman sobre la imagen del footer fijo */
        .footer-space {
          height: 85px; /* Un poco más alto que el .footer-img para dejar margen */
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
    //await axios.delete(`https://crud-alumnos-spring.uc.r.appspot.com/alumnos/eliminar-alumnos/${id}`);
    await axios.delete(`http://localhost:8081/alumnos/eliminar-alumnos${id}`);
    swal.fire({
        icon: 'success',
        title: 'Alumno Eliminado Correctamente',
        showConfirmButton: false,
        timer: 1500
    });
    console.log(`Alumno con ID ${id} eliminado`);
    await cargarAlumnos(); // Recargamos la lista de alumnos después de eliminar uno
  } catch (error) {
    console.error('Error al eliminar el alumno:', error);
    swal.fire({
      icon: 'error',
      title: 'Error al eliminar el alumno',
      text: 'No se pudo eliminar el alumno.',
    });
  }
}

onMounted(cargarAlumnos); // Llamamos a la función cargarAlumnos cuando el componente se monta


</script>

<template>
  

  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Alumnos</h2>
          <form 
          @submit.prevent="agregarAlumno" >
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="nombre" class="form-label" >Nombre</label>
                <input type="text" placeholder="Inserte su Nombre" class="form-control" maxlength="30" id="nombre" v-model="nuevoAlumno.nombre"
                  required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="apellidos" class="form-label">Apellidos</label>
                <input type="text" placeholder="Apellido Paterno y Apellido Materno" class="form-control" maxlength="30" id="apellidos" v-model="nuevoAlumno.apellido"
                  required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="carrera" class="form-label">Carrera</label>
                <select id="carrera" class="form-select"  style="border-radius:8px; border: 2px solid #a78bfa;" required v-model="nuevoAlumno.carrera">
                  <option value="" disabled>Selecciona una Opción</option>
                  <option value="Ingenieria en Mecatronica">Ingenieria en Mecatronica</option>
                  <option value="Ingenieria en Gestion Empresarial">Ingenieria en Gestion Empresarial</option>
                  <option value="Ingenieria Industrial">Ingenieria Industrial</option>
                  <option value="Ingenieria Civil">Ingenieria Civil</option>
                  <option value="Ingenieria en Sistemas Computacionales">Ingenieria en Sistemas Computacionales</option>
                  <option value="Licenciatura en Contador Público">Licenciatura en Contador Público</option>
                  <option value="Licenciatura en Arquitectura">Licenciatura en Arquitectura</option>
                  <option value="Licenciatura en Arquitectura">Licenciatura en Administración</option>
                </select>

              </div>
              <div class="col-md-6 mb-3">
                <label for="telefono" class="form-label">Telefono</label>
                <input type="text" placeholder="953*******" name="telefono" maxlength="10" class="form-control" id="telefono"
                  v-model="nuevoAlumno.telefono" required>
              </div>
              <div class="col-md-6 mb-3">
                <Label for="email" class="form-label"  >Correo electronico</Label>
                <input type="text" placeholder="user@tlaxiaco.tecnm.mx" name="email" maxlength="64" class="form-control" id="email" v-model="nuevoAlumno.email">
              </div>
              <div class="col-md-6 mb-3">
                <label for="imagenURL" class="form-label">Imagen URL</label>
                <input type="text" placeholder="Enlace URL" class="form-control" id="imagenURL" v-model="nuevoAlumno.imagenURL">
              </div>
            </div>
            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Alumno' : 'Agregar Alumno' }}
            </button>
          </form>
        </div>
      </div>

    </div>

  </div>
<div class="container">

    <TablaAlumnos
      v-for="carrera in carreras"
      :key="carrera"
      :carrera="carrera"
      :datos="filtrarPorCarrera(carrera)"
      @editar="editarAlumnos"
      @eliminar="eliminarAlumno"
      @imprimir="imprimirAlumnos"
    />
  </div>

</template>

<style scoped>
/* Fondo general */
.container {
  max-width: 1400px;
  margin: auto;
  padding: 30px 24px;
  background-color: #f3f4f6;
  border-radius: 15px;
}

/* Estilo de las tarjetas */
.card {
  border-radius: 16px;
  border: none;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(30, 58, 138, 0.1);
}

/* Títulos */
h2,
h5 {
  font-weight: bold;
  color: #1e3a8a;
  margin-bottom: 20px;
  text-align: center;
}

/* Etiquetas */
label {
  font-weight: 600;
  color: #374151;
}

/* Inputs */
input.form-control {
  border-radius: 10px;
  border: 2px solid #a78bfa;
  transition: 0.3s ease-in-out;
}
input.form-control:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 8px #a78bfa;
}

/* Botones */
button.btn {
  border-radius: 10px;
  font-weight: 600;
  margin-top: 5px;
  padding: 10px 18px;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background-color: #1e3a8a;
}

.btn-danger {
  background-color: #ef4444;
  border-color: #ef4444;
  color: white;
}

.btn-danger:hover {
  background-color: #b91c1c;
}

.btn-warning {
  background-color: #facc15;
  border-color: #facc15;
  color: #1e3a8a;
}

.btn-warning:hover {
  background-color: #eab308;
  color: white;
}

/* Tabla */
.table {
  background-color: #ffffff;
  border-radius: 12px;
  overflow: hidden;
}

.table th {
  background-color: #a78bfa;
  color: white;
  text-align: center;
  font-weight: 600;
}

.table td {
  text-align: center;
  vertical-align: middle;
}

/* Imagen en tabla */
table img {
  border-radius: 50%;
  object-fit: cover;
  width: 50px;
  height: 50px;
  border: 2px solid #3b82f6;
}

/* Hover en tabla */
.table-hover tbody tr:hover {
  background-color: #f0f0ff;
}
</style>

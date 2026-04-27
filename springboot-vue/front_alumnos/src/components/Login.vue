<script setup>
import { ref } from 'vue'
import axios from 'axios'

const emit = defineEmits(['login-success'])

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const login = async () => {
  error.value = ''
  loading.value = true
  try {
    const res = await axios.post('http://localhost:8081/auth/login', {
      username: username.value,
      password: password.value
    })
    const token = res.data.token
    if (token) {
      localStorage.setItem('token', token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      emit('login-success')
    } else {
      error.value = 'Respuesta sin token'
    }
  } catch (e) {
    error.value = e?.response?.data?.message || 'Credenciales inválidas'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="card p-4 mb-4">
    <h4 class="text-center">Iniciar sesión</h4>
    <div class="mb-3">
      <input v-model="username" class="form-control" placeholder="Usuario" />
    </div>
    <div class="mb-3">
      <input v-model="password" type="password" class="form-control" placeholder="Contraseña" />
    </div>
    <div class="d-flex justify-content-between align-items-center">
      <button class="btn btn-primary" @click="login" :disabled="loading">{{ loading ? 'Entrando...' : 'Entrar' }}</button>
      <div class="text-danger" v-if="error">{{ error }}</div>
    </div>
  </div>
</template>

<style scoped>
.card { max-width: 420px; margin: 16px auto; }
</style>

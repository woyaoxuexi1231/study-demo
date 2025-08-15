import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCounterStore = defineStore('counter', () => {
    const count = ref(0)
    const name = ref('Vue3')

    const doubleCount = computed(() => count.value * 2)

    function increment() {
        count.value++
    }

    function setName(newName) {
        name.value = newName
    }

    return { count, name, doubleCount, increment, setName }
})
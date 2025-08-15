import { ref } from 'vue'

export function useFetch(url) {
    const data = ref()
    const error = ref()
    const loading = ref(false)

    const fetchData = async () => {
        try {
            loading.value = true
            const response = await fetch(url)
            if (!response.ok) throw new Error('请求失败')
            data.value = await response.json()
        } catch (err) {
            error.value = err.message
        } finally {
            loading.value = false
        }
    }

    return { data, error, loading, fetchData }
}
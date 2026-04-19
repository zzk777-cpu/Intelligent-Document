import http from './http'

export const createDoc = (data) => http.post('/docs', data)
export const listDocs = (data) => http.post('/docs/search', data)
export const getDoc = (id) => http.get(`/docs/${id}`)
export const runIntelligence = (id) => http.post(`/docs/${id}/intelligence`)
export const deleteDoc = (id) => http.delete(`/docs/${id}`)

import request from '@/utils/request'

export function recursiveTopology() {
  return request({
    url: '/model/relation/recursiveTopology',
    method: 'get'
  })
}

export function listAllRelation() {
  return request({
    url: '/model/relation/listAll',
    method: 'get'
  })
}

export function listRelation(modelId) {
  return request({
    url: '/model/relation/list',
    method: 'get',
    params: { modelId }
  })
}

export function deleteRelation(id) {
  return request({
    url: '/model/relation/delete',
    method: 'post',
    params: { id }
  })
}


export function listAll() {
  return request({
    url: '/model/listAll',
    method: 'get'
  })
}


export function addRelation(data) {
  return request({
    url: '/model/relation/add',
    method: 'post',
    data
  })
}

export function listModel(name) {
  return request({
    url: '/model/list',
    method: 'get',
    params: { name }
  })
}


export function addModel(data) {
  return request({
    url: '/model/add',
    method: 'post',
    data
  })
}

export function deleteModel(id) {
  return request({
    url: '/model/delete',
    method: 'post',
    params: { id }
  })
}

export function updateModel(data) {
  return request({
    url: '/model/update',
    method: 'post',
    data
  })
}

export function listAttr(data) {
  return request({
    url: '/model/attribute/list',
    method: 'post',
    data
  })
}

export function deleteAttr(modelId, id) {
  return request({
    url: '/model/attribute/delete',
    method: 'post',
    params: { modelId, id }
  })
}

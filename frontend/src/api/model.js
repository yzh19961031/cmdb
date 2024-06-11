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

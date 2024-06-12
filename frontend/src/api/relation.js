import request from '@/utils/request'

export function list() {
  return request({
    url: '/relation/list',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/relation/add',
    method: 'post',
    data
  })
}

export function deleteRel(id) {
  return request({
    url: '/relation/delete',
    method: 'post',
    params: { id }
  })
}

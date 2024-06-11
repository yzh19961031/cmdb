import request from '@/utils/request'

export function list() {
  return request({
    url: '/group/list',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/group/add',
    method: 'post',
    data
  })
}

export function drop(id) {
  return request({
    url: '/group/delete',
    method: 'post',
    params: { id }
  })
}

export function update(data) {
  return request({
    url: '/group/update',
    method: 'post',
    data
  })
}

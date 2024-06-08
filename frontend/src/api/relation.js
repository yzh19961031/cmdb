import request from '@/utils/request'

export function list() {
  return request({
    url: '/relation/list',
    method: 'get'
  })
}

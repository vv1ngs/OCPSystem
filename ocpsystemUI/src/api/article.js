import request from '@/utils/request'
import qs from 'qs'
export function fetchList(query) {
  return request({
    url: '/operationUser/getAllUser',
    method: 'get',
    params: query
  })
}
export function fetchRoleList() {
  return request({
    url: '/role/getAllRole',
    method: 'get'
  })
}
export function allocationRole(data, data2) {
  return request({
    url: '/operationUser/allocationRole?rid=' + data.join() + '&uid=' + data2.join(),
    method: 'get'
    // params: { data, data2 },
    // paramsSerializer: params => {
    //   return qs.stringify(params, { arrayFormat: 'repeat' })
    // }
  })
}
export function fetchArticle(id) {
  return request({
    url: '/vue-element-admin/article/detail',
    method: 'get',
    params: { id }
  })
}
export function addOperationUser(data) {
  return request({
    url: '/operationUser/addOperationUser',
    method: 'post',
    data
  })
}
export function startOrStopUser(data, flag) {
  return request({
    url: '/operationUser/startOrStopUser?uid=' + data.join() + '&flag=' + flag,
    method: 'get'
  })
}
export function searchUser(username, status) {
  return request({
    url: '/operationUser/searchUser',
    method: 'post',
    data: qs.stringify({ 'username': username, 'status': status })
  })
}
export function fetchPv(pv) {
  return request({
    url: '/vue-element-admin/article/pv',
    method: 'get',
    params: { pv }
  })
}
export function getRoleByOperationUser(username) {
  return request({
    url: '/role/getRoleByOperationUser',
    method: 'post',
    params: { username }
  })
}
export function delOperationUser(uid) {
  return request({
    url: '/operationUser/delOperationUser?uid=' + uid.join(),
    method: 'get'
  })
}
export function createArticle(data) {
  return request({
    url: '/vue-element-admin/article/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/vue-element-admin/article/update',
    method: 'post',
    data
  })
}
export function updateOperationUser(data) {
  return request({
    url: '/operationUser/updateOperationUser',
    method: 'post',
    data
  })
}

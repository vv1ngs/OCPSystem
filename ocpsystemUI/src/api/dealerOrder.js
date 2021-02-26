import request from '@/utils/request'
import qs from 'qs'
var configs = {
  headers: { 'Content-Type': 'multipart/form-data' }
}
export function getAllDealerId(data) {
  return request({
    url: '/dealerOrder/getAllDealerId',
    method: 'get'
  })
}
export function getAllProduct(data) {
  return request({
    url: '/dealerOrder/getAllProduct?dealerId=' + data,
    method: 'get'
  })
}

export function getInfo(token) {
  return request({
    url: '/operationUser/getUserInfo',
    method: 'get',
    params: { token }
  })
}
export function addOrder(dealerId, value1, value2, value3, pid, cid, status) {
  return request({
    url: '/dealerOrder/addOrder',
    method: 'post',
    data: qs.stringify({
      'dealerId': dealerId,
      'getProductYear': value3,
      'getProductMonth': value2,
      'getProductMonthDetailed': value1,
      pid,
      cid,
      'status': status
    }, { arrayFormat: 'brackets' })
  })
}
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}
export function getAllOrder() {
  return request({
    url: '/dealerOrder/getAllOrder',
    method: 'get'
  })
}
export function verifyOrder(orderId, status, reason) {
  return request({
    url: '/order/verifyOrder',
    method: 'post',
    data: qs.stringify({ 'status': status, 'orderId': orderId, 'reason': reason })
  })
}
export function uploadFile(orderId, file) {
  const fd = new FormData()
  fd.append('files', file)
  fd.append('orderId', orderId)
  return request({
    url: '/dealerOrder/uploadFile',
    method: 'post',
    data: fd,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
export function getAllOrderByStatus(status) {
  return request({
    url: '/order/getAllOrderByStatus',
    method: 'post',
    data: qs.stringify({ 'status': status })
  })
}
export function getFile(orderId) {
  return request({
    url: '/order/getFile',
    method: 'post',
    data: qs.stringify({ 'orderId': orderId })
  })
}
export function searchOrder(dealerName, dealerId, orderId, startAmount, endAmount, status) {
  return request({
    url: '/order/searchOrder',
    method: 'post',
    data: qs.stringify({ 'orderId': orderId, 'dealerName': dealerName, 'dealerId': dealerId, 'startAmount': startAmount, 'endAmount': endAmount, 'status': status })
  })
}

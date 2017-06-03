/**
 * 包装jquery的ajax请求，
 * 返回Promise
 * @param {type} opts
 * {
 *      url: "",
 *      params: {},
 *      success:function(json){},
 *      failure:function(json){}
 * }
 * @returns {void}
 */
import jquery from 'jquery'
export default function (opts) {
  return new Promise((resolve, reject) => {
    jquery.ajax({
      url: opts.url,
      data: {
        request: JSON.stringify(opts.params)
      },
      method: 'POST',
      success: function (data) {
        if (data !== '') {
          var json = JSON.parse(data)
          if (json.success && opts.success && typeof opts.success === 'function') {
            opts.success(json)
          } else if (!json.success && opts.failure && typeof opts.failure === 'function') {
            opts.failure(json)
          } else {
            alert(json.message)
          }
          resolve('请求成功,返回不为空')
        }
        resolve('请求成功，但返回为空')
      },
      error: function (data) {
        reject('请求失败,请检查你的网络连接')
      }
    })
  })
}


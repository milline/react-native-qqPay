import { NativeModules, Platform } from 'react-native';

const { QqWallet } = NativeModules;

function wrapApi(nativeFunc) {
  if (!nativeFunc) {
    return undefined;
  }
  return (...args) => {
    return new Promise((resolve, reject) => {
      nativeFunc.apply(null, [
        ...args,
        (error, result) => {
          if (!error) {
            return resolve(result);
          }
          if (typeof error === 'string') {
            return reject(new Error(error));
          }
          reject(error);
        },
      ]);
    });
  };
}
export const onBtnIsMqqInstalled = Platform.OS !== 'ios' ? wrapApi(QqWallet.onBtnIsMqqInstalled) : () => { return false };

export const onBtnIsMqqSupportPay = Platform.OS !== 'ios' ? wrapApi(QqWallet.onBtnIsMqqSupportPay) : () => { return false };

/**
 * qq pay
 * @param {Object} data
 * @param {String} data.appId
 * @param {long} data.nonce
 * @param {String} data.tokenId
 * @param {String} data.pubAcc
 * @param {String} data.pubAccHint
 * @param {String} data.bargainorId
 * @param {String} data.sigType
 * @param {String} data.sig
 * @returns {Promise}
 */
export function qqPay(data) {
  if (Platform.OS === 'ios') {
    return
  }
  function correct(actual, fixed) {
    if (!data[fixed] && data[actual]) {
      data[fixed] = data[actual];
      delete data[actual];
    }
  }

  correct('appid', 'appId');
  correct('tokenid', 'tokenId');
  correct('bargainorid', 'bargainorId');
  correct('pubacc', 'pubAcc');
  return QqWallet.onBtnMqqPay(data);
}
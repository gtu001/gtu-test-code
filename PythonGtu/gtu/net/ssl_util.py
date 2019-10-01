

import warnings
import contextlib

import requests
from urllib3.exceptions import InsecureRequestWarning
from gtu.error import errorHandler


'''
from gtu.net import ssl_util
'''



old_merge_environment_settings = requests.Session.merge_environment_settings

@contextlib.contextmanager
def no_ssl_verification():
    opened_adapters = set()

    def merge_environment_settings(self, url, proxies, stream, verify, cert):
        # Verification happens only once per connection so we need to close
        # all the opened adapters once we're done. Otherwise, the effects of
        # verify=False persist beyond the end of this context manager.
        opened_adapters.add(self.get_adapter(url))

        settings = old_merge_environment_settings(self, url, proxies, stream, verify, cert)
        settings['verify'] = False

        return settings

    requests.Session.merge_environment_settings = merge_environment_settings

    try:
        with warnings.catch_warnings():
            warnings.simplefilter('ignore', InsecureRequestWarning)
            yield
    finally:
        requests.Session.merge_environment_settings = old_merge_environment_settings

        for adapter in opened_adapters:
            try:
                adapter.close()
            except:
                pass


def get(url) :
    resp = None
    try:
        with no_ssl_verification():
            resp = requests.get(url)
            print('It works')

            resp = requests.get(url, verify=True)
            print('Even if you try to force it to')

        resp = requests.get(url, verify=False)
        print('It resets back')

        session = requests.Session()
        session.verify = True

        with no_ssl_verification():
            resp = session.get(url, verify=True)
            print('Works even here')

        try:
            resp = requests.get(url)
        except requests.exceptions.SSLError:
            print('It breaks')

        try:
            resp = session.get(url)
        except requests.exceptions.SSLError:
            print('It breaks here again')

    except Exception as ex :
        errorHandler.printStackTrace2(ex)
        raise ex

    return resp


if __name__ == '__main__' :
    # resp = ssl_util.get("https://xxxxxxxxx")
    # soup = bs(resp.text, "html.parser")
    pass

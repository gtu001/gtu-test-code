import urllib.request
import io

url = 'https://www.sec.gov/litigation/admin/2015/34-76574.pdf'
remote_file = urllib.request.urlopen(url).read()
memory_file = io.BytesIO(remote_file)


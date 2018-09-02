 #!/usr/bin/env python

from setuptools import setup, find_packages

setup(name='gtu_test',
    version='1.0',
    description='Python Distribution Utilities',
    author='Greg Ward',
    author_email='gtu001@gmail.com',
    url='https://www.python.org/sigs/distutils-sig/',
    packages=find_packages(),#exclude=['_test']
    zip_safe=False,
    setup_requires=[],  # 'nose>=1.0'
    install_requires=[
#         "gevent>=1.1.2",
#         "zerorpc>=0.6.0",
    ],
    package_data = {
        # If any package contains *.txt or *.rst files, include them:
        '': ['*.txt', '*.rst', '*.*'],
        # include any *.msg files found in the 'test' package, too:
        'test': ['*.msg'],
    },
    test_suite='nose.collector'
    )

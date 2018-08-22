
'''
 docker images \
    | fgrep -f <(docker inspect --format='{{.Id}} {{.Parent}}' \
        $(docker images --all --quiet) \
        | python3 D/workstuff/gtu-test-code/PythonGtu/gtu/docker/docker_desc.py  <image_id> )
'''

#!/usr/bin/python3
import sys


def desc(image_ids, links):
    if links:
        link, *tail = links
        if len(link) > 1:
            image_id, parent_id = link
            checkid = lambda i: parent_id.startswith(i)
            if any(map(checkid, image_ids)):
                return desc(image_ids | {image_id}, tail)
        return desc(image_ids, tail)
    return image_ids


def gen_links(lines):
    parseid = lambda s: s.replace('sha256:', '')
    for line in reversed(list(lines)):
        yield list(map(parseid, line.split()))


if __name__ == '__main__':
    image_ids = {sys.argv[1]}
    links = gen_links(sys.stdin.readlines())
    trunc = lambda s: s[:12]
    print('\n'.join(map(trunc, desc(image_ids, links))))

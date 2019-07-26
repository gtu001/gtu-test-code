import argparse


ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True,
    help="path to input image")
ap.add_argument("-p", "--prototxt", required=True,
    help="path to Caffe 'deploy' prototxt file")
ap.add_argument("-m", "--model", required=True,
    help="path to Caffe pre-trained model")
ap.add_argument("-l", "--labels", required=True,
    help="path to ImageNet labels (i.e., syn-sets)")
args = vars(ap.parse_args())


for (i, key) in enumerate(args):
    print(i , key, "\t", args[key])

    
if __name__ == '__main__' :
    print("done...")
    
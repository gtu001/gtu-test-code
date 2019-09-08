import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil


def test1():
    x = tf.Variable(tf.random_normal([10, 784], mean=10, stddev=5, dtype=tf.float32, name="x"))
    init = tf.constant_initializer(value=0)
    W = tf.get_variable("W", [784, 10], initializer=init)
    b = tf.get_variable("b", [10], initializer=init)
    output = tf.nn.softmax(tf.matmul(x, W) + b)
    init_op = tf.initialize_all_variables()
    with tf.Session() as sess :
        sess.run(init_op)

        x1 = sess.run(x)
        print("x1", x1)

        output_result = sess.run(output)
        print("x", x)
        print("W", W)
        print("b", b)
        print("output", output)
        print("output_result", output_result)


 '''  標準差測試'''
def test2() :
    x = tf.Variable(tf.random_normal([1000, 3], mean=10, stddev=0.6, dtype=tf.float32, name="x"))
    init_op = tf.initialize_all_variables()
    with tf.Session() as sess :
        sess.run(init_op)
        x1 = sess.run(x)
        matplotlibUtil.hist(x1)



if __name__ == '__main__' :
        test2()
    # checkSelf.checkMembersToHtml(tf, "tensorlow_api")

    #import matplotlib
    #checkSelf.checkMembersToHtml(matplotlib.pyplot, "matplotlib.pyplot")
    
        print("done...")
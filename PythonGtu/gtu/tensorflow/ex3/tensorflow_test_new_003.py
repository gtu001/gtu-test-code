import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf


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



if __name__ == '__main__' :
    #test1()
    checkSelf.checkMembersToHtml(tf, "tensorlow_api")
    print("done...")
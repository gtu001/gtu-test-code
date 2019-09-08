import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil


def layer(input, weight_shape, bias_shape) :
    weight_init = tf.random_uniform_initializer(minval = -1, maxval = 1)
    bias_init = tf.constant_initializer(value=0)
    W = tf.get_variable("W", weight_shape, initializer=weight_init)
    b = tf.get_variable("b", bias_shape, initializer=bias_init)
    return tf.matmul(input, W) + b


def my_network(input) :
    with tf.variable_scope("layer_1") :
        output_1 = layer(input, [784, 100], [100])
    with tf.variable_scope("layer_2") :
        output_2 = layer(output_1, [100,50], [50])
    with tf.variable_scope("layer_3") :
        output_3 = layer(output_2, [50, 100], [100])
    return output_3


def test1() :
    i_1 = tf.placeholder(tf.float32, [1000, 784], name="i_1")
    final_output = my_network(i_1)
    init_op = tf.initialize_all_variables()
    with tf.Session() as sess : 
        sess.run(init_op)
        i_1_p = tf.Variable(tf.random_normal(shape=[1000,784], mean=10, stddev=1))
        print("tf.Session()", tf.Session())
        final_output_result = sess.run(final_output, feed_dict={i_1 : i_1_p})
        print(final_output_result)
    print("done...test1")

if __name__ == '__main__' :
       # test1()
    # checkSelf.checkMembersToHtml(tf, "tensorlow_api")

    #import matplotlib
    #checkSelf.checkMembersToHtml(matplotlib.pyplot, "matplotlib.pyplot")
    checkSelf.checkMembersToHtml(tf.Session(), "tf.Session")
    print("done...")
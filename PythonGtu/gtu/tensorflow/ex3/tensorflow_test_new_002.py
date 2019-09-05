import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf


def test() :
    deep_learning = tf.constant('Deep Learning')
    session = tf.Session()
    val1 = session.run(deep_learning)
    a = tf.constant(2)
    b = tf.constant(2)
    multiply = tf.multiply(a, b)
    val2 = session.run(multiply)
    print("val1", val1)
    print("val2", val2)


def test2():
    weights = tf.Variable(tf.random_normal([300, 200], stddev=0.5), name="weights")
    init_op = tf.initialize_all_variables()
    session = tf.Session()
    session.run(init_op)
    weights2 = session.run(weights)
    print("init_op", init_op)
    print("weights", weights)
    print("weights2", weights2)


def getTestValue(label, val, feed_dict) :
    init_op = tf.initialize_all_variables()
    sess = tf.Session()
    sess.run(init_op)
    result = sess.run(val, feed_dict=feed_dict)
    print("getTestValue(" + label + ")", result)
    sess.close()
    return result


def test3():
    x = tf.placeholder(tf.float32, (None), name="x")
    y = tf.reduce_sum(x)
    feed_dict = {x : [1,2,3,4]}
    getTestValue("x", y, feed_dict)


def test4() :
    x = tf.placeholder(tf.float32, (None), name="x")
    y = tf.reduce_sum(x)
    npVal = np.random.rand(2, 2)
    print(npVal)
    print("00", npVal[0,0])
    print("01", npVal[0,1])
    print("10", npVal[1,0])
    print("11", npVal[1,1])
    feed_dict = {x : npVal}
    getTestValue("q", y, feed_dict)


def test5() :
    x = tf.placeholder(tf.float32, (None, 784), name='x')
    W = tf.Variable(tf.random_normal([784, 10], -1, 1), name='W')
    b = tf.Variable(tf.zeros([10]), name='biases')
    output = tf.matmul(x, W) + b
    init_op = tf.initialize_all_variables()
    sess = tf.Session()
    sess.run(init_op)
    feed_dict = {x : np.random.rand(1, 784)}
    result = sess.run(output, feed_dict=feed_dict)
    print("result", result)


def test6():
    input = tf.Variable(tf.random_normal([100, 784], 10, 3), name="input")
    W_1 = tf.Variable(tf.random_uniform([784, 100], -1, 1), name="W_1")
    b_1 = tf.Variable(tf.zeros([100]), name="biases_1")
    output_1 = tf.matmul(input, W_1) + b_1
    W_2 = tf.Variable(tf.random_uniform([100, 50], -1, 1), name="W_2")
    b_2 = tf.Variable(tf.zeros([50]), name="biases_2")
    output_2 = tf.matmul(output_1, W_2) + b_2
    W_3 = tf.Variable(tf.random_uniform([50, 10], -1, 1), name="W_3")
    b_3 = tf.Variable(tf.zeros([10]), name="biases_3")
    output_3 = tf.matmul(output_2, W_3) + b_3
    # printing names
    print("Printing names of weight parameters")
    print(W_1.name, W_2.name, W_3.name)
    print("Printing names of bias parameters")
    print(b_1.name, b_2.name, b_3.name)
    init_op = tf.initialize_all_variables()
    sess = tf.Session()
    sess.run(init_op)
    output_1_result = sess.run(output_1)
    output_2_result = sess.run(output_2)
    output_3_result = sess.run(output_3)
    print("output_1_result", output_1_result)
    print("output_2_result", output_2_result)
    print("output_3_result", output_3_result)


def test7():
    input = tf.Variable(tf.random_normal([10, 3], 10, 3), name="input")
    init_op = tf.initialize_all_variables()
    sess = tf.Session()
    sess.run(init_op)
    result = sess.run(input)
    print("result", result)
    for i,v in enumerate(result) :
        print(i, v)



if __name__ == '__main__' :
    test7()
    # checkSelf.checkMembersToHtml(tf, "tensorlow_api")
    print("done...")
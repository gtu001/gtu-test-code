import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil
# from tensorflow.examples.tutorials import mnist

class GtuMySummary() :
    def __init__(self, session) :
        self.session = session
        self.saver = tf.train.Saver()
        self.summary_writer = tf.summary.FileWriter("logistic_logs/", graph_def=session.graph_def)

    def addSummary(self, feed_dict, op) :
        self.summary_op = tf.summary.merge_all()
        summary_str = self.session.run(self.summary_op, feed_dict=feed_dict)
        self.summary_writer.add_summary(summary_str, self.session.run(op))

    def writeFile(self, global_step) :
        self.saver.save(self.session, "logistic_logs/model-checkpoint", global_step=global_step)



def test1() :
    x = tf.Variable(tf.random_normal([10, 784], mean=10, stddev=0.6, dtype=tf.float32, name="x"))
    
    # x = tf.placeholder(tf.float32, (None, 784), name='x')
    W = tf.Variable(tf.random_normal([784, 10], -1, 1), name='W')
    b = tf.Variable(tf.zeros([10]), name='biases')
    output = tf.matmul(x, W) + b

    init_op = tf.initialize_all_variables()

    with tf.Session() as sess :
        sess.run(init_op)
        summary = GtuMySummary(sess)
        sess.run(output)
        summary.addSummary({}, output)
        summary.write(10)


if __name__ == '__main__' :

    checkSelf.checkMembersToHtml(tf.summary, "tf.summary.htm")

    print("done...")





                

                
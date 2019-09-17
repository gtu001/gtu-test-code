import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil
from tensorflow.examples.tutorials import mnist

class GtuMySummary() :
    def __init__(session) :
        self.session = session
        self.summary_op = tf.summary.merge_all()
        self.summary_writer = tf.summary.FileWriter("logistic_logs/", graph_def=session.graph_def)

    def addSummary(self, feed_dict, op) :
        summary_str = sess.run(self.summary_op, feed_dict=feed_dict)
        self.summary_writer.add_summary(summary_str, self.session.run(op))

    def writeFile(self, global_step) :
        saver.save(self.session, "logistic_logs/model-checkpoint", global_step=global_step)







                

                
import tensorflow as tf
import numpy as np
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil
from tensorflow.examples.tutorials import mnist


def layer(input, weight_shape, bias_shape):
    weight_stddev = (2.0/weight_shape[0]) ** 0.5
    w_init = tf.random_normal_initializer(stddev=weight_stddev)
    bias_init = tf.constant_initializer(value=0)
    W = tf.get_variable("W", weight_shape, initializer=w_init)
    b = tf.get_variable("b", bias_shape, initializer=bias_init)
    return tf.nn.relu(tf.matmul(input, W) + b)


def inference(x) :
    init = tf.constant_initializer(value=0)
    W = tf.get_variable("W", [784,10], initializer=init)
    b = tf.get_variable("b", [10], initializer=init)
    output = tf.nn.softmax(tf.matmul(x, W) + b)
    return output


def loss(output, y) :
    dot_product = y * tf.log(output)
    xentropy = - tf.reduce_sum(dot_product)
    loss = tf.reduce_mean(xentropy)
    return loss


def training(cost, global_step) :
    learning_rate = 10
    tf.summary.scalar("cost", cost)
    optimizer = tf.train.GradientDescentOptimizer(learning_rate)
    train_op = optimizer.minimize(cost, global_step=global_step)
    return train_op


def evaluate(output, y) :
    correct_prediction = tf.equal(tf.argmax(output, 1), tf.argmax(y, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    return accuracy


def test1() :
    learning_rate = 0.01
    training_epochs = 1000
    batch_size = 100
    display_step = 1

    with tf.Graph().as_default() :
        # mnist data image of shape 28*28=784
        x = tf.placeholder("float", [None, 784])
        # 0-9 digits recognition => 10 classes
        y = tf.placeholder("float", [None, 10])

        output = inference(x)

        cost = loss(output, y)

        global_step = tf.Variable(0, name='global_step', trainable=False)

        train_op = training(cost, global_step)

        eval_op = evaluate(output, y)

        summary_op = tf.summary.merge_all()

        saver = tf.train.Saver()

        sess = tf.Session()

        summary_writer = tf.summary.FileWriter("logistic_logs/", graph_def=sess.graph_def)

        init_op = tf.initialize_all_variables()

        sess.run(init_op)

        #Training cycle
        for epoch in range(training_epochs):
            avg_cost = 0.
            total_batch = int(mnist.train.num_examples/batchsize)

            #Loop over all batches
            for i in range(total_batch) :
                mbatch_x, mbatch_y = mnist.train.next_batch(batch_size)
                #Fit training using batch data
                feed_dict = {x : mbatch_x, y : mbatch_y}
                sess.run(train_op, feed_dict=feed_dict)
                #Compute average loss
                minibatch_cost = sess.run(cost, feed_dict=feed_dict)
                avg_cost += minibatch_cost/total_batch
            
            #Display logs per epoch step
            if epoch % display_step == 0:
                val_feed_dict = {
                    x : mnist.validation.images,
                    y : mnist.validation.labels
                }
                accuracy = sess.run(eval_op, feed_dict=val_feed_dict)
                print("Validation Error : ", (1-accuracy))

                summary_str = sess.run(summary_op, feed_dict=feed_dict)

                summary_writer.add_summary(summary_str, sess.run(global_step))

                saver.save(sess, "logistic_logs/model-checkpoint", global_step=global_step)

        print("Optimization Finished!")

        test_feed_dict = {
            x : mnist.test.images,
            y : mnist.test.labels
        }

        accuracy = sess.run(eval_op, feed_dict=test_feed_dict)

        print("Test Accuracy: ", accuracy)




if __name__ == '__main__' :
    test1()
    print("done...")
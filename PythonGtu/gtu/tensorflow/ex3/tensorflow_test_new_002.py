import tensorflow as tf
from gtu.reflect import checkSelf


def test() :
    deep_learning = tf.constant('Deep Learning')
    session = tf.Session()
    session.run(deep_learning)
    a = tf.constant(2)
    b = tf.constant(2)
    multiply = tf.multiply(a, b)
    session.run(multiply)



if __name__ == '__main__' :
    # test()
    checkSelf.checkMembersToHtml(tf, "test1111")
    print("done...")
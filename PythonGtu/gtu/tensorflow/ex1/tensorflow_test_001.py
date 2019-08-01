import tensorflow as tf

'''
tf.enable_eager_execution()
tf.add(1, 2).numpy()

hello = tf.constant('Hello, TensorFlow!')
hello.numpy()
'''


h = tf.constant("Hello")
w = tf.constant(" World!")
hw = h + w 

print(hw)

with tf.Session() as sess:
    ans = sess.run(hw)
    print (ans)
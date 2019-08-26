import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, LSTM, Bidirectional#, CuDNNLSTM
from tensorflow.keras.callbacks import ModelCheckpoint
import pickle
import numpy as np

def main():
    pickle_in = open("X_normalized.pickle","rb")
    X = pickle.load(pickle_in)

    pickle_in = open("y.pickle","rb")
    y = pickle.load(pickle_in)

    y = np.array(y)
    X = np.array(X)

    print(X.shape)
    print(X[0].shape)
    print(y.shape[1])

    model = create_model()

    weights_filepath = "weights/weights-improvement-{epoch:02d}-{val_loss:.4f}-{val_acc:.4f}-bigger.hdf5"

    checkpoint = ModelCheckpoint(weights_filepath, monitor='val_loss', verbose=0, save_best_only=True, mode='min')

    callbacks_list = [checkpoint]

    model.fit(X,
              y,
              epochs=40,
              validation_split=0.2, callbacks = callbacks_list)


def create_model():
    model = Sequential()
    # IF you are running with a GPU, try out the CuDNNLSTM layer type instead (don't pass an activation, tanh is required)
    #model.add(Bidirectional(LSTM(128, input_shape=(50, 4), return_sequences=True, activation='relu')))
    model.add(LSTM(128, input_shape=(50, 3), activation='relu', return_sequences=True))
    model.add(Dropout(0.2))

    #model.add(Bidirectional(LSTM(128, return_sequences=True, activation='relu')))
    #model.add(Dropout(0.2))

    model.add(LSTM(128, activation='relu'))
    model.add(Dropout(0.1))

    model.add(Dense(32, activation='relu'))
    model.add(Dropout(0.2))

    model.add(Dense(3, activation='sigmoid'))  #softmax

    opt = tf.keras.optimizers.Adam(lr=0.001, decay=1e-6)


    # Compile model
    model.compile(
        loss='binary_crossentropy',  #sparse_categorical_crossentropy'
        optimizer=opt,
        metrics=['accuracy'],
    )
    return model




if __name__ == '__main__':
    main()

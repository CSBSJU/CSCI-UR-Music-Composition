import tensorflow as tf
from os import listdir
from os.path import isfile, join
import sys, glob, os, random, pickle
import numpy


meta_tokens = {"<Start>" : 0.0,
            "<Note>" : 1.0,
            "<Unused>" : 2.0,
            "<Pad>" : 3.0}

intervals = {}      #2n -2 for positive
                    #2n - 1 for negative

durations = {}      #n for all

dotted = {"<Not-Dotted>" : 0.0,
            "<Dotted>" : 1.0,
            "<Double-Dotted>" : 2.0}


''' README

line 60 is hardcoded to 50 (the length of notes used for each **kern file)
line 100 is hardcoded to 50 and 3 denoting the shape of a notes for a file



'''

def main():
# Txt read
    args = sys.argv[1:]
    print("Starting to find max length...........")
    max_file_length, mean_file_length = find_max_length(args[0])
    print("Max File Length: {}".format(max_file_length))
    print("Mean File Length: {}".format(mean_file_length))

    print("Starting to read durations..........")
    durations, dotted = read_durations(args[0], max_file_length)
    print("Completed, starting to read intervals.........")

    intervals, meta = read_intervals(args[1], max_file_length)
    print("Completed, starting to create class array............")
    classes = create_class_array(args[2])
    #print(intervals)
    #print(classes)
    #print(durations)
    durations_max = 0
    for sample in durations:
        sample_max = max(sample)
        if sample_max > durations_max:
            durations_max = sample_max
    print("Durations Max Value: {}".format(durations_max))

    intervals_max = 0
    for sample in intervals:
        sample_max = max(sample)
        if sample_max > intervals_max:
            intervals_max = sample_max
    print("Intervals Max Value: {}".format(intervals_max))


    print("Completed, starting to format training data")
    training_data = []
    for i in range(len(durations)):
        temp = []
        for j in range(50):  #Mean file length
            temp.append([intervals[i][j], durations[i][j], dotted[i][j]])
            #temp.append(durations[i][j])
        training_data.append([temp, classes[i]])

    print(classes)

    #print(training_data[])

    print("Shuffing training data")

    random.shuffle(training_data)

    X = []
    y = []

    done = False
    for features,label in training_data:
        if not done:
            done = True
            print(features)
        print(label)
        X.append(features)
        y.append(label)

    print(X[0])
    pickle_out = open("X.pickle","wb")
    pickle.dump(X, pickle_out)
    pickle_out.close()

    X = normalize_data(X)

    X = numpy.reshape(X, (len(X), 50, 3))
    #X = numpy.array(X)
    #X = X / 48.0
    print("\nNormalizing: ------>\n")

    print(X[0])

    pickle_out = open("X_normalized.pickle","wb")
    pickle.dump(X, pickle_out)
    pickle_out.close()

    pickle_out = open("y.pickle","wb")
    pickle.dump(y, pickle_out)
    pickle_out.close()


def normalize_data(X):
    max = 0.0
    for sample in X:
        for vector in sample:
            for point in vector:
                if point > max:
                    max = point

    print("Max Point: {}".format(max))
    for sample in range(len(X)):
        for vector in range(len(X[sample])):
            for point in range(len(X[sample][vector])):
                X[sample][vector][point] = X[sample][vector][point] / max

    return X


def find_max_length(dir):

    list_of_files = os.listdir(dir)

    max = 0
    total_lines = 0.0
    total_files = 0.0

    for entry in list_of_files:
        full_path = os.path.join(dir, entry)
        if os.path.isdir(full_path):

            text_files = glob.glob(full_path + '/*.krn')
            for file in text_files:
                total_files += 1
                count = 0
                input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
                lines = input1.split("\n")

                for line in lines:
                    if line:
                        count += 1
                total_lines += count
                if count > max:
                    max = count
    mean = total_lines/total_files
    return max, mean


def create_class_array(dir):
    list_of_files = os.listdir(dir)

    i = 0

    classes = []

    for entry in list_of_files:
        full_path = os.path.join(dir, entry)

        if os.path.isdir(full_path):

            text_files = glob.glob(full_path + '/*.krn')

            # print(text_files)

            for file in text_files:
                if i == 0:
                    classes.append([1, 0, 0])
                elif i == 1:
                    classes.append([0, 1, 0])
                else:
                    classes.append([0, 0, 1])
                #classes.append(i)
        i += 1


    return classes


def read_intervals(dir, max_length):
    global meta_tokens

    list_of_files = os.listdir(dir)

    final_intervals = []
    final_meta = []

    done = False
    for entry in list_of_files:
        full_path = os.path.join(dir, entry)

        if os.path.isdir(full_path):
            text_files = glob.glob(full_path + '/*.krn')
            print(len(text_files))
            for file in text_files:
                if not done:
                    print("First File Read: {}".format(file))
                    done = True
                input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
                intervals = input1.split("\n")

                formatted_intervals = [0.0] #add 0 for start of sequence
                formatted_meta = [meta_tokens.get("<Start>")]
                #TODO: use "<Start>" here instead

                for i in intervals:
                    try:
                        #if i.startswith("["):
                            #formatted_intervals.append(float(0))
                        if i.startswith("r"):
                            formatted_intervals.append(float(0))
                            formatted_meta.append(meta_tokens.get("<Note>"))
                        elif str(i).startswith("1"):
                            formatted_intervals.append(float(1))
                            formatted_meta.append(meta_tokens.get("<Note>"))
                        elif int(i) > 1:
                            formatted_intervals.append(float(int(i)*2-2))
                            formatted_meta.append(meta_tokens.get("<Note>"))
                        elif int(i) < -1:
                            formatted_intervals.append(float(int(i)*-2-1))
                            formatted_meta.append(meta_tokens.get("<Note>"))
                    except:
                        #if i:
                        #    print("Error in {}".format(file))
                        pass
                for i in range(len(formatted_intervals), max_length):
                    formatted_intervals.append(float(0))
                    formatted_meta.append(meta_tokens.get("<Pad>"))
                    #TODO: use "<Pad>" here instead

                final_meta.append(formatted_meta)
                final_intervals.append(formatted_intervals)

    return final_intervals, final_meta


def read_durations(dir, max_length):

    global dotted

    final_durations = []
    final_dotted_amount =[]

    list_of_files = os.listdir(dir)

    done = False
    for entry in list_of_files:
        full_path = os.path.join(dir, entry)

        if os.path.isdir(full_path):
            text_files = glob.glob(full_path + '/*.krn')

            for file in text_files:
                if not done:
                    print("First Duration File: {}".format(file))
                    done = True
                input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
                durations = input1.split("\n")

                formatted_durations = []
                formatted_dotted_amount = []

                for d in durations:
                    try:
                        if d.endswith(".."):
                            num = float(d[:-2])
                            formatted_durations.append(num)
                            formatted_dotted_amount.append(float(dotted.get("<Double-Dotted>")))

                        elif d.endswith("."):
                            num = float(d[:-1])
                            formatted_durations.append(num)
                            formatted_dotted_amount.append(float(dotted.get("<Dotted>")))
                        else:
                            formatted_durations.append(float(d))
                            formatted_dotted_amount.append(float(dotted.get("<Not-Dotted>")))
                    except:
                        #if d:
                        #    print("Error in {}".format(file))
                        pass
                for i in range(len(formatted_durations), max_length):
                    formatted_durations.append(float(0))
                    formatted_dotted_amount.append(float(0))
                    #TODO: Use padding here

                final_durations.append(formatted_durations)
                final_dotted_amount.append(formatted_dotted_amount)
    return final_durations, final_dotted_amount

main()
'''
max = find_max_length("./test_kerns/test_durations")
print("Max Length: {}".format(max))
intervals, meta = read_intervals("./test_kerns/test_intervals", max)

durations, dotted = read_durations("./test_kerns/test_durations", max)

for i in range(len(intervals)):
    for j in range(len(meta[0])):
        print("meta: {}\tinterval: {}\tduration: {}\tdotted: {}".format(meta[i][j], intervals[i][j], durations[i][j], dotted[i][j]))
    print("NEXT FILE: \n\n\n")
'''



#---Shuffle data

# Python3 program to create target string, starting from
# random string using Genetic Algorithm

import random

import RRN_mnist

import numpy, pickle

# Number of individuals in each generation
POPULATION_SIZE = 50

DEFAULT_GNOME_LENGTH = 100

MAX_GENE_NUM = 1024



PADDING = 3


''' README

Line 212 contains a hardcoded value for the model to be loaded in.  Ideally
this should be dynamically given, but simply change this line to the loaction
of a different model if you so choose to.

Line 85 normalizes the data by dividing by the max point found in the
training data.  This should not be hardcoded ideally.

Line 257 has hardcoded values for reshaping input for prediction of the model
'''


class Individual(object):

    def __init__(self, notes, model):
        self.notes = notes
        self.size = len(notes)
        self.genes = self.notes_to_genes()
        self.model = model
        self.fitness = self.cal_fitness()

    def notes_to_genes(self):
        genes = []
        for note in self.notes:
            genes.append(Gene(note))
        return genes

    def mate(self, par2):
        global PADDING
        child_gene = []
        for gp1, gp2 in zip(self.genes, par2.genes):

            if gp1.interval == 0 and gp1.duration == 0 and gp1.dotted == 0:
                #child_gene.append([gp2.meta, gp2.interval, gp2.duration, gp2.dotted])
                child_gene.append([gp2.interval, gp2.duration, gp2.dotted])
            elif gp2.interval == 0 and gp2.duration == 0 and gp2.dotted == 0:
                #child_gene.append([gp1.meta, gp1.interval, gp1.duration, gp1.dotted])
                child_gene.append([gp1.interval, gp1.duration, gp1.dotted])
            else:

                # random probability
                prob = random.random()

                # if prob is less than 0.45, insert gene
                # from parent 1
                if prob < 0.40:
                    #child_gene.append([gp1.meta, gp1.interval, gp1.duration, gp1.dotted])
                    child_gene.append([gp1.interval, gp1.duration, gp1.dotted])
                elif prob < 0.5:
                    child_gene.append(gp1.mutated_genes())
                elif prob < 0.90:
                    #child_gene.append([gp2.meta, gp2.interval, gp2.duration, gp2.dotted])
                    child_gene.append([gp2.interval, gp2.duration, gp2.dotted])
                else:
                    child_gene.append(gp2.mutated_genes())

            # create new Gene using
        # generated chromosome for offspring
        return Individual(child_gene, self.model)

    def cal_fitness(self):

        neural_net = self.model.get_model()
        #normalize everything
        #X = self.notes[:50]
        X = numpy.reshape(self.notes, (1, 50, 3))
        X = X / 96.0
        prediction = neural_net.predict(X)

        #print("Fitness : {} ____________________________________".format(prediction))
        return prediction[0][2]

    def convert_to_midi_notes(self):

        intervals = []
        durations = []
        for gene in self.genes:
            if not (gene.interval == 0.0 and gene.duration == 0.0 and gene.dotted == 0.0):
                rest = ""
                if gene.interval == 0:
                    rest = "r"
                    #intervals.append("r")
                elif gene.interval == 1:
                    intervals.append("1")
                elif gene.interval % 2 == 0:
                    intervals.append(str((gene.interval + 2)/2))
                else:
                    intervals.append(str(-(gene.interval + 1)/2))

                if gene.dotted == 1:
                    durations.append(str(gene.duration)+"."+rest)
                elif gene.dotted == 2:
                    durations.append(str(gene.duration)+".."+rest)


        return durations, intervals


class Gene(object):
    '''
    Class representing gene in chromosome
    '''

    def __init__(self, note):

        #self.meta = note[0]
        self.interval = note[0]
        self.duration = note[1]
        self.dotted = note[2]

    def mutated_genes(self):
        '''
        #create random genes for mutation
        '''
        global GENES

        #mutate_gene = random.randint(0,1)
        new_interval = self.interval
        new_duration = self.duration
        new_dotted = self.dotted
        mutate_gene = 0 #TODO: add duration mutation
        mutate_gene = random.random()
        if mutate_gene <= 0.45:  #mutate interval
            if self.interval % 2 == 0 and self.interval > 0: #interval was ascending
                delta = random.randint(0,1)
                if delta == 0 and self.interval < 14: #increase interval by one (ie 4 goes to 5)
                    new_interval += 2
                else:
                    self.interval -= 2  #decrease interval
            elif self.interval > 1: #not rest, descending interval
                delta = random.randint(0, 1)
                if delta == 0 and self.interval < 15:  # increase interval by one (ie -4 goes to -5)
                    new_interval += 2
                else:
                    new_interval -= 2  # decrease interval
            elif  self.interval == 1: #interval is unison (ie repeats last note)
                delta = random.randint(0,3)
                if delta == 3:
                    new_interval = 0 # change interval to results
                else:
                    new_interval += delta # change unison to either rest, +step, -step, or repeat unison
            else: #self.interval == 0
                delta = random.randint(0,1)
                new_interval += delta
        elif mutate_gene <= 0.9: # mutate durations
            delta = random.random()
            if delta <= 0.33:
                if new_duration > 32:
                    new_duration = int(new_duration/2.0)
                else:
                    new_duration *= 2
            elif delta <= 0.66:
                if new_duration == 0:
                    new_duration = 1
                else:
                    new_duration = int(new_duration/2.0)
            else:
                delta = random.randint(0,1)
                if delta == 0:
                    new_duration -= 1
                else:
                    new_duration += 1

        else:
            new_dotted = random.randint(0,2)


        return [new_interval, new_duration, new_dotted]



def create_gnome(default_length):
    global MAX_GENE_NUM
    global PADDING

    rand_length = random.randint(25, 50)

    gnome = []

    for i in range(rand_length):
        gnome.append([random.randint(0, 15), random.randint(1, 15), random.randint(0, 2)])
    for i in range(rand_length, 50):
        gnome.append([0, 0, 0])
    return gnome

class Model(object):
    def __init__(self):

        self.model = self.setup_model()

    def setup_model(self):
        model = RRN_mnist.create_model()

        model.load_weights('weights/weights-improvement-34-0.4333-0.8149-bigger.hdf5')

        return model

    def get_model(self):
        return self.model


def write_notes(durations, intervals, file_name):
    f = open(file_name + ".txt", "w+")

    f.write(durations[0])
    for i in range(1, len(durations)):
        f.write(", {}".format(durations[i]))

    f.write("\r\n{}".format(intervals[0]))
    for i in range(1, len(intervals)):
        f.write(", {}".format(intervals[i]))

    f.close()


def main():
    global POPULATION_SIZE
    global DEFAULT_GNOME_LENGTH

    # current generation
    generation = 1

    found = False
    population = []
    model = Model()

    pickle_in = open("X_normalized.pickle","rb")
    X = pickle.load(pickle_in)

    pickle_in = open("y.pickle","rb")
    y = pickle.load(pickle_in)

    y = numpy.array(y)
    X = numpy.array(X)

    for i in range(10):
        prediction = model.get_model().predict(numpy.reshape(X[i], (1, 50, 3)))
        print("{}-Prediction: {}".format(i,prediction))
        print("{}-Actual: {}\n".format(i, y[i]))


    # create initial population
    for _ in range(POPULATION_SIZE):
        gnome = create_gnome(DEFAULT_GNOME_LENGTH)
        #print(gnome)
        population.append(Individual(gnome, model))

    while not found:

        # sort the population in increasing order of fitness score
        population = sorted(population, key=lambda x: x.fitness, reverse=True)
        #print(population)
        # fitness of 0 means we have reached goal
        if generation % 10 == 0:
            for i in range(10):
                out_durations, out_intervals = population[i].convert_to_midi_notes()
                write_notes(out_durations, out_intervals, "notes/Generation_{}_Individual_{}".format(generation, i))

        if population[0].fitness >= 0.99:
            found = True
            break

        # Otherwise generate new offsprings for new generation
        new_generation = []

        # Perform Elitism, save top %10 of population for next generation
        s = int((10 * POPULATION_SIZE) / 100)
        new_generation.extend(population[:s])

        # top 50% of population will be chosen from for mating
        s = int((90 * POPULATION_SIZE) / 100)
        for _ in range(s):
            parent1 = random.choice(population[:50])
            parent2 = random.choice(population[:50])
            child = parent1.mate(parent2)
            new_generation.append(child)

        population = new_generation

        print(
            "Generation: {}\tFitness: {}". \
            format(generation,
                   population[0].fitness))

        generation += 1


    print(
                "Generation: {}\tOptimal Fitness: {}". \
                format(generation,
                       population[0].fitness))
    count = 0
    for gene in population[0].genes:
        print("Gene {}: [{}, {}, {}]".format(count, gene.interval, gene.duration, gene.dotted))
        count += 1

if __name__ == '__main__':
    main()

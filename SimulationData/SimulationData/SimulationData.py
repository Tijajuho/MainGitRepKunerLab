import numpy as np;
import matplotlib.pyplot as plt;
from datetime import date;

class SimulationData(object):
    """ 
    creates artificial data set for demixing
    _channel should be "left" or "right"
    """
    def __init__(self,path,filename,channel=None):
        self._path = path;
        self._filename = filename;
        self._channel = channel;
        self.createSimulationData();
    
    def loadData(self):
        f = open(self._path+self._filename,'r');
        self.__intensities = np.zeros(len(f.readlines()));
        self.__counts = np.zeros(len(self.__intensities));
        f.close();
        f = open(self._path+self._filename,'r');
        for counter,line in enumerate(f):
            if counter > 0:
                if self._channel == "left":
                    self.__intensities[counter-1]=line.split(' ')[0];
                    self.__counts[counter-1]=line.split(' ')[1];
                elif self._channel == "right":
                    self.__intensities[counter]=line.split(' ')[2];
                    self.__counts[counter]=line.split(' ')[3];
                elif self._channel == "nochannels":
                    self.__intensities[counter-1]=line.split(' ')[0];
                    self.__counts[counter-1]=line.split(' ')[1];
                else:
                    print "Can't read __intensities out of file."
                    break;
        f.close();
    
    def calculateK(self):
        self.loadData();
        
        k = np.average(-(np.log(self.__counts[0:13])/self.__intensities[0:13]));#with self-established histogram data: 11:30, with paper-data: 0:13
        
        return k;
    
    def distributeExponentially(self,k):
        minIntensity = self.__intensities[0];
        maxIntensity = 70000.;
        allCounts = 1000000;
        self.intens = np.arange(minIntensity,maxIntensity,1);
#         self._intensDistro = np.round(1500*np.e**(k*self.intens)).astype(int);#with self-established histogram data: 9000, with paper-data: 500
        exponDistro = (1*np.e**(k*self.intens))/np.sum(1*np.e**(k*self.intens));
        
        preDistro = np.random.choice(self.intens,allCounts,p = exponDistro).astype(int);
        self._setDistros = np.sort(np.asarray(list(set(preDistro)) ));
        self._intensDistro = np.zeros(len(self._setDistros));
        for i,x in enumerate(self._setDistros):
            self._intensDistro[i] = preDistro.tolist().count(x);
            print str(i) + " of " + str(len(self._setDistros));
        self._intensDistro = self._intensDistro.astype(int);
        
    def createSimulationData(self):
        k = self.calculateK();
        
        self.distributeExponentially(k);
#         test = 1*expon.pdf(self.intens,scale=-1/k)).astype(int); #+np.round(3*np.random.rand(len(self.intens)))
        
        self.saveData();
        self.plotData();
        self.constructEndDistroNoSaving();
        
        ######### ExponentialRandomVerteilung nachschauen
        
    def constructEndDistroNoSaving(self):
        self.endDistro = np.zeros(np.sum(self._intensDistro));
        for i in range(len(self._setDistros)):
            for j in range(self._intensDistro[i]):
                if i !=0:
                    self.endDistro[np.sum(self._intensDistro[:i:])+j] = self._setDistros[i];
                else:
                    self.endDistro[j] = self._setDistros[i];
                    
    def saveData(self):
        newFilename = self._path+str(date.today())+"ArtificialDataSet_improved.txt"
        
        self.endDistro = np.zeros(np.sum(self._intensDistro));
        f = open(newFilename,'w');
        f.write("Based on %s;\n" % self._filename);
        for i in range(len(self._setDistros)):
            for j in range(self._intensDistro[i]):
                f.write("%s\n" % (self._setDistros[i]));
                if i !=0:
                    self.endDistro[np.sum(self._intensDistro[:i:])+j] = self._setDistros[i];
                else:
                    self.endDistro[j] = self._setDistros[i];
        
        f.close();
        
    def plotData(self): 
        fig1 = plt.figure();
        axID = fig1.add_subplot(1,1,1);
        axID.plot(self._setDistros,np.flipud(np.sort(self._intensDistro)),'.');
        #axID.plot(self.endDistro,'r')
        axID.text(0.9,0.5,str(np.sum(self._intensDistro))); #,horizontalalignment='left',verticalalignment='top');
        fig1.show();
        a = 1;
        # print np.arange(minIntensity,maxIntensity,(maxIntensity-minIntensity)/allCounts);


path = '\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\'
# filename = "HistoDataIntensROUND_150818PhaloidinAlexa647MitochondriaCF680Messung2pt2.txt"
filename = "HistoDataIntensROUND_150908PaperAlexa647HistoDataEstimation.txt"
sd = SimulationData(path, filename,channel="nochannels");

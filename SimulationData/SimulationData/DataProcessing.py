from SimulationData import SimulationData;
import numpy as np;
import os.path;
from datetime import date;
import sys;

class DataProcessing(object):
    def __init__(self, path, filename, rawDataFile,channel = None):
        self.__simuData = SimulationData(path,filename,channel);
        self.__rawDataFile = rawDataFile;
        self.__path = path;
        self.__filename = filename;
        self.__dataLib = {'Alexa647':"\\\\129.206.158.175\\FN-Praktikant\\Timm\\SpectraRawEtc\\Alexa647EmissionSpectrum.txt",
                          'CF680':"\\\\129.206.158.175\\FN-Praktikant\\Timm\\SpectraRawEtc\\CF680EmissionSpectrum.txt",
                          'Dichroic':"\\\\129.206.158.175\\FN-Praktikant\\Timm\\SpectraRawEtc\\F33-692Strahlteiler.txt"};
        self.processData();
    
    def processData(self):
        self.loadData(self.__rawDataFile);
        self.estimateBlinkingWavelengths()
        
        
    def loadData(self,rawDataFile):
        """ Enter fLuorophor or other value from lib in constructor as string
        """
        f = open(self.__dataLib[rawDataFile],'r');
        self.__wavelengths = np.zeros(len(f.readlines()));
        self.__probabilities = np.zeros(len(self.__wavelengths))
        f.close();
        
        f = open(self.__dataLib[rawDataFile],'r');
        for counter,line in enumerate(f):
            if counter > 0:
                self.__wavelengths[counter]=int(line.split(' ')[0]);
                self.__probabilities[counter]=float(line.split(' ')[1]);
        f.close();
    
    def estimateBlinkingWavelengths(self):
        
        counter = 0;
        for j,distr in enumerate(self.__simuData.endDistro):
            if (j-1 == -1) or (distr != self.__simuData.endDistro[j-1]):
                counter +=1;
        self.__tmpIntens = np.zeros(counter)
        
        counter = 0;      
        iCounter = 0;
        chunkNumber = 100;
        chunkSize = len(self.__simuData.endDistro)/chunkNumber;
        saveCounter = 0;
        parameterList = [0,chunkNumber,chunkSize,counter,iCounter];
        saveParamList = [0,chunkSize,saveCounter,0]
        
        for chunk in range(chunkNumber):
            parameterList[0] = chunk;
            saveParamList[0] = chunk;
            print "processing data..."
            parameterList = self.estimateBWInnerLoop(parameterList);
            print "saving data..."
            saveParamList = self.saveWaveDistros(saveParamList)
            if saveParamList[2] == -1:
                sys.exit("interrupting program")
                
        
    def estimateBWInnerLoop(self,parameterList):
        chunk = parameterList[0]
        chunkNumber = parameterList[1]
        chunkSize = parameterList[2]
        counter = parameterList[3]
        iCounter = parameterList[4]
        
        if (chunk == chunkNumber-1):
            tmpData = self.__simuData.endDistro[chunk*chunkSize::]
            self.__waveDistros = np.zeros(len(tmpData)).tolist();
        else:
            tmpData = self.__simuData.endDistro[chunk*chunkSize:(chunk+1)*chunkSize];
            self.__waveDistros = np.zeros(len(tmpData)).tolist();
            
        for i,intensity in enumerate(tmpData):
            self.__waveDistros[i] = np.random.choice(self.__wavelengths,intensity,p=self.__probabilities).astype(int);
            if ((counter-1 == -1) or (intensity != self.__simuData.endDistro[counter-1])):
                print iCounter;
                self.__tmpIntens[iCounter] = intensity;
                iCounter += 1;
            counter += 1;
            
        parameterList[0] = chunk;
        parameterList[1] = chunkNumber;
        parameterList[2] = chunkSize;
        parameterList[3] = counter;
        parameterList[4] = iCounter;
        return parameterList;
    
    def checkUserInput(self,message):
        answer = raw_input(message)
        if answer == "n":
            return "n"
        elif answer == "y":
            return "y"
        else:
            print "Please enter y for yes or n for no."
            self.checkUserInput();
        
    def saveWaveDistros(self,saveParamList):
        chunk = saveParamList[0];
        chunkSize = saveParamList[1];
        saveCounter = saveParamList[2];
        bEventCounter = saveParamList[3];
        
        dataFile = self.__dataLib[self.__rawDataFile]
        
        newDir = str(date.today()) + dataFile.split("\\")[len(dataFile.split("\\"))-1][:len(self.__rawDataFile):] + "\\"
        newFilename = str(chunk) + "_" + str(date.today()) + "_waveDistrosinBlinkingEvents_" + self.__filename.split("_")[1]
        
        if not(os.path.exists(self.__path + newDir)):
            os.mkdir(self.__path + newDir)
        #else:
        #    if self.checkUserInput("Directory %s already exists. Files inside will be overwritten. (y/n)" % (self.__path + newDir)) == "n":
        #        return -1;
 
        if not(os.path.isfile(self.__path + newDir + newFilename)):
            f = open(self.__path + newDir + newFilename,'w');
            f.write("Estimation of the wavelengths of individual photons within each blinking event.\n")
            for blinkingDistro in range(chunkSize*chunk,chunkSize*chunk+len(self.__waveDistros)):
                if (blinkingDistro-1 == -1) or (self.__simuData.endDistro[blinkingDistro] != self.__simuData.endDistro[blinkingDistro-1]):
                    f.write("intensity: %s\n" % str(self.__tmpIntens[saveCounter]));
                    saveCounter += 1;
                    bEventCounter = 0;
                    print saveCounter;
                bEventCounter += 1;
                f.write("blinking event no. %s\n[" % (bEventCounter))
                for wavelength in self.__waveDistros[blinkingDistro-chunkSize*chunk]:
                    f.write(str(wavelength) + " ");
                f.write("]\n")
            f.close();
        else:
            if self.checkUserInput("File %s already exists. Overwrite? (y/n)" % newFilename) == "n":
                return -1;
            else:
                f = open(self.__path + newDir + newFilename,'w');
                f.write("Estimation of the wavelengths of individual photons within each blinking event.\n")
                for blinkingDistro in range(chunkSize*chunk,chunkSize*chunk+len(self.__waveDistros)):
                    if (blinkingDistro-1 == -1) or (self.__simuData.endDistro[blinkingDistro] != self.__simuData.endDistro[blinkingDistro-1]):
                        f.write("intensity: %s\n" % str(self.__tmpIntens[saveCounter]));
                        saveCounter += 1;
                        bEventCounter = 0;
                        print saveCounter;
                    bEventCounter+=1;
                    f.write("blinking event no. %s\n[" % (bEventCounter))
                    for wavelength in self.__waveDistros[blinkingDistro-chunkSize*chunk]:
                        f.write(str(wavelength) + " ");
                    f.write("]\n")
                    #f.write(np.array_str(self.__waveDistros[blinkingDistro-chunkSize*chunk]) + "\n");
                f.close();
        saveParamList[0] = chunk;
        saveParamList[1] = chunkSize;
        saveParamList[2] = saveCounter;
        saveParamList[3] = bEventCounter;
        return saveParamList;

    
path = '\\\\129.206.158.175\\FN-Praktikant\\Timm\\Alexa647\\'
filename = "HistoDataIntensROUND_150908PaperAlexa647HistoDataEstimation.txt"
rawDataFile = "Alexa647"

dp = DataProcessing(path,filename,rawDataFile,channel="nochannels");

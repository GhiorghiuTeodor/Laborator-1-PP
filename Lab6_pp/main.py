import os
import struct
from abc import ABC, abstractmethod


class GenericFile(ABC):
    @abstractmethod
    def get_path(self):
        raise NotImplementedError("Nu a fost implementata metoda get_path")

    @abstractmethod
    def get_freq(self):
        raise NotImplementedError("Nu a fost implementata metoda get_freq")

    @abstractmethod
    def calc_freq(self, content):
        raise NotImplementedError("Nu a fost implementata metoda calc_freq")


class TextASCII(GenericFile):
    def __init__(self, path, content):
        self.absolute_path = path
        self.frequencies = self.calc_freq(content)

    def get_path(self):
        return self.absolute_path

    def get_freq(self):
        return self.frequencies

    def calc_freq(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content:
            freq[byte] += 1
        return freq


class TextUNICODE(GenericFile):
    def __init__(self, path, content):
        self.absolute_path = path
        self.frequencies = self.calc_freq(content)

    def get_path(self):
        return self.absolute_path

    def get_freq(self):
        return self.frequencies

    def calc_freq(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content:
            freq[byte] += 1
        return freq


class Binary(GenericFile):
    def __init__(self, path, content):
        self.absolute_path = path
        self.frequencies = self.calc_freq(content)

    def get_path(self):
        return self.absolute_path

    def get_freq(self):
        return self.frequencies

    def calc_freq(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content:
            freq[byte] += 1
        return freq


class XMLFile(TextASCII):
    def __init__(self, path, content):
        super().__init__(path, content)
        self.first_tag = self.get_first_tag(content)

    def get_first_tag(self, content):
        try:
            text = content.decode('ascii', errors='ignore')
            start = text.find('<')
            end = text.find('>', start)
            if start != -1 and end != -1 and '</' in text:
                return text[start:end + 1]
        except:
            return None
        return None

    def get_path(self):
        return self.absolute_path

    def get_freq(self):
        return self.frequencies


class BMP(Binary):
    def __init__(self, path, content):
        super().__init__(path, content)
        self.width, self.height, self.bpp = self.extract_info(content)

    def extract_info(self, content):
        if len(content) >= 30 and content[:2] == b'BM':
            width = struct.unpack('<I', content[18:22])[0]
            height = struct.unpack('<I', content[22:26])[0]
            bpp = struct.unpack('<H', content[28:30])[0]
            return width, height, bpp
        return None, None, None

    def show_info(self):
        return f'BMP File: {self.get_path()} - Width: {self.width}, Height: {self.height}, BPP: {self.bpp}'

    def get_path(self):
        return self.absolute_path

    def get_freq(self):
        return self.frequencies


def identify_file_type(path, content):
    if len(content) >= 30 and content[:2] == b'BM':
        bmp_file = BMP(path, content)
        return bmp_file

    tex = TextASCII(path, content)
    freq = tex.get_freq()

    total_chars = sum(freq.values())

    if total_chars == 0:
        return None

    ascii_ratio = sum(freq[i] for i in range(9, 128)) / total_chars
    unicode_zero_ratio = freq[0] / total_chars

    if ascii_ratio > 0.9 and unicode_zero_ratio < 0.01:
        text = content.decode('ascii', errors='ignore')
        if '<' in text and '>' in text and '</' in text:
            xml_file = XMLFile(path, content)
            return xml_file
        ascii_file = TextASCII(path, content)
        return ascii_file

    elif unicode_zero_ratio > 0.4:
        unicode_file = TextUNICODE(path, content)
        return unicode_file

    return binary_file


def scan_directory(directory):
    dictionar = {
        'xml': [],
        'unicode': [],
        'bmp': [],
        'ascii': [],
        'binary': []
    }

    for root, _, files in os.walk(directory):
        for file in files:
            file_path = os.path.join(root, file)
            if os.path.isfile(file_path):
                with open(file_path, 'rb') as f:
                    content = f.read()
                    identified_file = identify_file_type(file_path, content)
                    if isinstance(identified_file, XMLFile):
                        dictionar['xml'].append(identified_file.get_path())
                    elif isinstance(identified_file, TextUNICODE):
                        dictionar['unicode'].append(identified_file.get_path())
                    elif isinstance(identified_file, BMP):
                        dictionar['bmp'].append(identified_file.show_info())
                    elif isinstance(identified_file, TextASCII):
                        dictionar['ascii'].append(identified_file.get_path())
                    elif isinstance(identified_file, Binary):
                        dictionar['binary'].append(identified_file.get_path())
    return dictionar


directory_to_scan = "C:\\Users\\User\\Desktop\\Test"
scan_results = scan_directory(directory_to_scan)

print("XML Files:", scan_results['xml'])
print("UNICODE Files:", scan_results['unicode'])
print("BMP Files:", scan_results['bmp'])
print("ASCII Files:", scan_results['ascii'])

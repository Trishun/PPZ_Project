# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse
import subprocess


def index(request):
    if status():
        s = "online"
    else:
        s = "offline"
    return HttpResponse("Status: {}".format(s))

def status():
    try:
        status = int(subprocess.check_output("ps aux | grep '[j]ava serverMain' | awk '{print $2}'", shell=True))
    except ValueError:
        status = 0
    return status

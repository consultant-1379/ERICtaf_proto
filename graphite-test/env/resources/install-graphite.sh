#!/bin/bash

# EPEL and base stuff
rpm -Uvh \
    http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
yum install -y gcc gcc-c++ zlib-devel glibc-devel rpm-build \
    curl curl-devel openssl openssl-devel wget \
    python python-devel python-pip rabbitmq-server \
    bitmap bitmap-fonts cairo pycairo httpd memcached mod_python mod_wsgi

# RabbitMQ
/usr/lib/rabbitmq/bin/rabbitmq-plugins enable rabbitmq_management
service rabbitmq-server restart

# Install Graphite
cat >> /tmp/graphite_reqs.txt << EOF
Django==1.3
python-memcached==1.53
django-tagging==0.3.1
Twisted==11.1.0
whisper==0.9.10
carbon==0.9.10
graphite-web==0.9.10
EOF
pip install -r /tmp/graphite_reqs.txt

# Configure carbon
cp /opt/graphite/conf/carbon.conf.example /opt/graphite/conf/carbon.conf
sed \
    -e '/^# AMQP_/s/^# *//' \
    -e '/^# ENABLE_AMQP/s/^# *//' \
    -e '/^ENABLE_AMQP /s/=.*$/= True/g' \
    -e '/^AMQP_VERBOSE /s/=.*$/= True/g' \
    -e '/^AMQP_METRIC_NAME_IN_BODY /s/=.*$/= True/g' \
    -i /opt/graphite/conf/carbon.conf

# Configure whisper
cat >> /tmp/storage-schemas.conf << EOF
[stats]
priority = 110
pattern = .*
retentions = 1s:1h,10s:6h,1m:7d,10m:1y
EOF
cp /tmp/storage-schemas.conf /opt/graphite/conf/storage-schemas.conf

# Configure Graphite front end
cp /home/vagrant/resources/graphite.conf /etc/httpd/conf.d/graphite.conf
cp /opt/graphite/conf/graphite.wsgi.example /opt/graphite/conf/graphite.wsgi
cp /opt/graphite/webapp/graphite/local_settings.py.example \
    /opt/graphite/webapp/graphite/local_settings.py
mkdir -p /var/run/wsgi
cd /opt/graphite/webapp/graphite/ && python manage.py syncdb --noinput
chown -R apache:apache /opt/graphite/storage/

# Configure carbon
cp /home/vagrant/resources/carbon-cache /etc/init.d/carbon-cache
chmod +x /etc/init.d/carbon-cache

# Run stuff
service memcached restart
service carbon-cache restart
service httpd restart

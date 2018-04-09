require 'json'
package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |spec|
  spec.name           = "VWOReactNative"
  spec.version        = package['version']
  spec.summary        = package['description']
  spec.description    = package['description']
  spec.license        = package['license']
  spec.author         = package['author']
  spec.source         = { :git => "https://github.com/wingify/vwo-react-native.git", :tag => "master" }
  spec.requires_arc   = true
  spec.platform       = :ios, '8.0'
  spec.source_files   = "VWOReactNative/**/*.{h,m}"
  spec.dependency 'React'
  spec.dependency 'VWO', '~>2.2'
end


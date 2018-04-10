
Pod::Spec.new do |spec|
  spec.name         = "VWOReactNative"
  spec.version      = "1.0.0"
  spec.summary      = "VWOReactNative"
  spec.description  = <<-DESC
                  VWO React Native
                   DESC
  spec.homepage     = ""
  spec.license      = "MIT"
  # spec.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  spec.author       = { "wingify" => "info@wingify.com" }
  spec.platform     = :ios, "9.0"
  spec.source       = { :git => "https://github.com/wingify/vwo-react-native.git", :tag => "master" }
  spec.source_files = "VWOReactNative/**/*.{h,m}"
  spec.requires_arc = true
  spec.dependency 'React'
  spec.dependency 'VWO', '2.2.1'

end

  
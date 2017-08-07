
Pod::Spec.new do |s|
  s.name         = "VWOReactNative"
  s.version      = "1.0.0"
  s.summary      = "VWOReactNative"
  s.description  = <<-DESC
                  VWO React Native
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/wingify/vwo-react-native.git", :tag => "master" }
  s.source_files  = "VWOReactNative/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  s.dependency "VWO"

end

  
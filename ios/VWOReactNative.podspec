
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
  spec.author             = { "author" => "author@domain.cn" }
  spec.platform     = :ios, "8.0"
  spec.source       = { :git => "https://github.com/wingify/vwo-react-native.git", :tag => "master" }
  spec.source_files  = "VWOReactNative/**/*.{h,m}"
  spec.requires_arc = true
  spec.default_subspec = 'Dynamic'
  spec.dependency 'React'

  spec.subspec 'Dynamic' do |dynamic|
    dynamic.dependency 'VWO', '~>2.0.0-beta1'
  end
  spec.subspec 'Static' do |static|
    static.dependency 'VWO', '~>2.0.0-beta1'
  end

end

  